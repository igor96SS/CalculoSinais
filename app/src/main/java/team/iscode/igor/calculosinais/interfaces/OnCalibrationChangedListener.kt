package team.iscode.igor.calculosinais.interfaces

interface OnCalibrationChangedListener {
    fun onOutputValueChanged(position: Int, newPercentageValue: Float, newInputValue: Float)
}