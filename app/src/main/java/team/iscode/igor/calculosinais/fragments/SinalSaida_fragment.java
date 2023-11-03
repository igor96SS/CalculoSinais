package team.iscode.igor.calculosinais.fragments;

import static java.lang.Math.sqrt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import team.iscode.igor.calculosinais.R;


public class SinalSaida_fragment extends Fragment {

    TextView valoresSaida, valoresEntrada, saida;

    EditText valorEntrada, zeroEntrada, zeroSaida, cemEntrada, cemSaida;
    Button btn_calc;
    double sRaiz, cRaiz, rangeOut, rangeIn, valorIn, zeroIn, zeroOut, cemIn, cemOut;


    public SinalSaida_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewInflater = inflater.inflate(R.layout.fragment_sinal_saida_fragment, container, false);

        valorEntrada = viewInflater.findViewById(R.id.valorEntrada);
        zeroEntrada = viewInflater.findViewById(R.id.zeroEntrada);
        zeroSaida = viewInflater.findViewById(R.id.zeroSaida);
        cemEntrada = viewInflater.findViewById(R.id.cemEntrada);
        cemSaida = viewInflater.findViewById(R.id.cemSaida);
        btn_calc = viewInflater.findViewById(R.id.btnCalc);
        valoresEntrada = viewInflater.findViewById(R.id.valoresEntrada);
        valoresSaida = viewInflater.findViewById(R.id.valoresSaida);
        saida = viewInflater.findViewById(R.id.saida);

        btn_calc.setOnClickListener(this::calc);
        valoresEntrada.setOnClickListener(this::limparValoresEntrada);
        valoresSaida.setOnClickListener(this::limparValoresSaida);
        saida.setOnClickListener(this::erase);


        return viewInflater;
    }

    public void limparValoresEntrada(View v){
        valorEntrada.getText().clear();
        zeroEntrada.getText().clear();
        cemEntrada.getText().clear();
        zeroEntrada.requestFocus();
    }

    public void limparValoresSaida(View v){
        zeroSaida.getText().clear();
        cemSaida.getText().clear();
        zeroSaida.requestFocus();
    }

    public void erase(View view) {
        valorEntrada.getText().clear();
    }

    public void calc(View view) {
        if(valorEntrada.getText().toString().isEmpty() || zeroSaida.getText().toString().isEmpty() || zeroEntrada.getText().toString().isEmpty() || cemSaida.getText().toString().isEmpty() || cemEntrada.getText().toString().isEmpty()){

            AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
            adb.setTitle("ALERTA");
            adb.setMessage("Preencha todos os Campos");
            adb.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = adb.create();
            alert.show();
        }
        else{
            valorIn = Double.parseDouble(valorEntrada.getText().toString());
            zeroIn = Double.parseDouble(zeroEntrada.getText().toString());
            zeroOut = Double.parseDouble(zeroSaida.getText().toString());
            cemIn = Double.parseDouble(cemEntrada.getText().toString());
            cemOut = Double.parseDouble(cemSaida.getText().toString());

            DecimalFormat formater = new DecimalFormat("#.##");

            rangeOut = cemOut-zeroOut;

            rangeIn = cemIn-zeroIn;

            sRaiz = (((valorIn - zeroIn)*rangeOut)/rangeIn)+zeroOut;

            cRaiz = (sqrt((sRaiz-zeroOut)/rangeOut)*rangeOut)+zeroOut;

            AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
            adb.setTitle("RESULTADOS");
            adb.setMessage("Range Saída: " + formater.format(rangeOut)
                    + System.lineSeparator()
                    + "Range Entrada: " + formater.format(rangeIn)
                    + System.lineSeparator()
                    + "Sinal Saída Linear: " + formater.format(sRaiz)
                    + System.lineSeparator()
                    + "Caudais Não Lineares: " + formater.format(cRaiz));
            adb.setPositiveButton("Sair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = adb.create();
            alert.show();
        }
    }
    
}