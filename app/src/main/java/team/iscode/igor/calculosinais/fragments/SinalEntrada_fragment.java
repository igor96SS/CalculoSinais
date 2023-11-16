package team.iscode.igor.calculosinais.fragments;

import static java.lang.Math.pow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import team.iscode.igor.calculosinais.R;

public class SinalEntrada_fragment extends Fragment {

    TextView valoresSaida, valoresEntrada, saida;

    EditText valorSaida, zeroEntrada, zeroSaida, cemEntrada, cemSaida;
    Button btn_calc;
    double sRaiz, cRaiz, rangeOut, rangeIn, valorOut, zeroIn, zeroOut, cemIn, cemOut;

    public SinalEntrada_fragment(){
        // Empty constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewInflate = inflater.inflate(R.layout.fragment_sinal_entrada_fragment, container, false);


        valorSaida = viewInflate.findViewById(R.id.valorSaida);
        zeroEntrada = viewInflate.findViewById(R.id.zeroEntrada);
        zeroSaida = viewInflate.findViewById(R.id.zeroSaida);
        cemEntrada = viewInflate.findViewById(R.id.cemEntrada);
        cemSaida = viewInflate.findViewById(R.id.cemSaida);
        btn_calc = viewInflate.findViewById(R.id.btnCalc);
        valoresEntrada = viewInflate.findViewById(R.id.valoresEntrada);
        valoresSaida = viewInflate.findViewById(R.id.valoresSaida);
        saida = viewInflate.findViewById(R.id.saida);


        btn_calc.setOnClickListener(this::calc);
        valoresEntrada.setOnClickListener(this::limparValoresEntrada);
        valoresSaida.setOnClickListener(this::limparValoresSaida);
        saida.setOnClickListener(this::erase);


        Bundle args = getArguments();
        if (args != null) {
            float zeroInput = args.getFloat("zeroInput", 0.0f);
            float zeroOutput = args.getFloat("zeroOutput", 0.0f);
            float cemInput = args.getFloat("cemInput", 0.0f);
            float cemOutput = args.getFloat("cemOutput", 0.0f);

            // Faça o que desejar com os valores, por exemplo, defina os campos de entrada
            zeroEntrada.setText(String.valueOf(zeroInput));
            zeroSaida.setText(String.valueOf(zeroOutput));
            cemEntrada.setText(String.valueOf(cemInput));
            cemSaida.setText(String.valueOf(cemOutput));
        }

        //Keyboard button
        TextView.OnEditorActionListener editorActionListener = (v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // Chama a ação do botão calcular
                btn_calc.performClick();
                return true;
            }
            return false;
        };

        //set focus so that keyboard change works on all edittexts
        valorSaida.setImeOptions(EditorInfo.IME_ACTION_DONE);
        zeroEntrada.setImeOptions(EditorInfo.IME_ACTION_DONE);
        zeroSaida.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cemEntrada.setImeOptions(EditorInfo.IME_ACTION_DONE);
        cemSaida.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //add listening to all edittexts
        valorSaida.setOnEditorActionListener(editorActionListener);
        zeroEntrada.setOnEditorActionListener(editorActionListener);
        zeroSaida.setOnEditorActionListener(editorActionListener);
        cemEntrada.setOnEditorActionListener(editorActionListener);
        cemSaida.setOnEditorActionListener(editorActionListener);



        return viewInflate;
    }

    public void limparValoresEntrada(View v){
        zeroEntrada.getText().clear();
        cemEntrada.getText().clear();
        zeroEntrada.requestFocus();
    }

    public void limparValoresSaida(View v){
        valorSaida.getText().clear();
        zeroSaida.getText().clear();
        cemSaida.getText().clear();
        zeroSaida.requestFocus();
    }

    public void erase(View view) {
        valorSaida.getText().clear();
    }


    public void calc(View view) {
        if(valorSaida.getText().toString().isEmpty() || zeroSaida.getText().toString().isEmpty() || zeroEntrada.getText().toString().isEmpty() || cemSaida.getText().toString().isEmpty() || cemEntrada.getText().toString().isEmpty()){

            AlertDialog.Builder adb = new AlertDialog.Builder(requireContext(), R.style.MyDialogTheme);
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
            valorOut = Double.parseDouble(valorSaida.getText().toString());
            zeroIn = Double.parseDouble(zeroEntrada.getText().toString());
            zeroOut = Double.parseDouble(zeroSaida.getText().toString());
            cemIn = Double.parseDouble(cemEntrada.getText().toString());
            cemOut = Double.parseDouble(cemSaida.getText().toString());

            DecimalFormat formater = new DecimalFormat("#.##");

            rangeOut = cemOut-zeroOut;

            rangeIn = cemIn-zeroIn;

            sRaiz = (((valorOut - zeroOut)*rangeIn)/rangeOut)+zeroIn;

            cRaiz = pow(sRaiz-zeroIn,2)/rangeIn + zeroIn;

            AlertDialog.Builder adb = new AlertDialog.Builder(requireContext(), R.style.MyDialogTheme);
            adb.setTitle("RESULTADOS");
            adb.setMessage("Range Saída: " + formater.format(rangeOut)
                    + System.lineSeparator()
                    + "Range Entrada: " + formater.format(rangeIn)
                    + System.lineSeparator()
                    + "Sinal Entrada Linear: " + formater.format(sRaiz)
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

    public void setValues(float zeroIn, float zeroOut, float cemIn, float cemOut) {
        // Use os valores passados para definir os campos do fragmento
        zeroEntrada.setText(String.valueOf(zeroIn));
        zeroSaida.setText(String.valueOf(zeroOut));
        cemEntrada.setText(String.valueOf(cemIn));
        cemSaida.setText(String.valueOf(cemOut));
    }

}