package com.example.scientificcalculator

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.lang.Exception
import java.text.DecimalFormat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {


    var calculation: TextView?=null
    var answer: TextView?=null
    var btn_RorD:Button?=null

    var sCalculation = ""
    var sAnswer = ""
    var number_one = ""
    var number_two = ""
    var current_oprator = ""
    var prev_ans = ""
    var RorD = "RAD"
    var sin_inv:String? = null
    var cos_inv:String? = null
    var tan_inv:String? = null
    var function:String? = null
    var Result: Double? = 0.0
    var numberOne:Double? = 0.0
    var numberTwo:Double? = 0.0
    var temp:Double? = 0.0
    var dot_present: Boolean? = false
    var number_allow:Boolean? = true
    var root_present:Boolean? = false
    var invert_allow:Boolean? = true
    var power_present:Boolean? = false
    var factorial_present: Boolean? = false
    var constant_present:Boolean? = false
    var function_present:Boolean? = false
    var value_inverted:Boolean? = false

    //we need to reformat answer
    var format: NumberFormat? = null
    var longformate:NumberFormat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        calculation = findViewById(R.id.calculation)
        answer = findViewById(R.id.answer)
        btn_RorD = findViewById(R.id.btn_RorD)

        //set movement to the textView
        calculation?.movementMethod = ScrollingMovementMethod()


        //we set the answer upto four decimal
        format = DecimalFormat("#.####")

        //we need to reformat answer if it's long
        longformate = DecimalFormat("0.#E0");


        sin_inv = R.string.sin_inverse.toString()
        cos_inv = R.string.cos_inverse.toString()
        tan_inv = R.string.tan_inverse.toString()

        btn_RorD?.setOnClickListener {

            RorD = btn_RorD?.text.toString()
           if (RorD.equals("RAD"))
                RorD = "DEG"
            else
               RorD = "RAD"

            btn_RorD?.text = RorD
        }

    }

    //-----Number----------------------
    fun onClickNumber(view: View){
        //we need to find which button is pressed
        if(number_allow as Boolean) {

            var bn = view as Button

            sCalculation += bn.text  //string concat for calculation
            number_one += bn.text    //string concat for ans

            numberOne = number_one.toDouble()


            if (function_present as Boolean) {
                calculateFunction(function);
                return;
            }

            //check root is present
            if (root_present as Boolean) {
                numberOne = Math.sqrt(numberOne!!.toDouble());
                Toast.makeText(this,numberOne.toString(),Toast.LENGTH_SHORT).show()
            }

            when (current_oprator) {

                "" -> {
                    //if current oprator is null
                    if (power_present as Boolean) {
                        temp = Result?.plus(Math.pow(numberTwo!!.toDouble(), numberOne!!.toDouble()))
                    } else {
                        temp = Result?.plus(numberOne!!.toDouble())
                    }
                }
                "+" -> {
                    if (power_present as Boolean) {
                        temp = Result?.plus(Math.pow(numberTwo!!.toDouble(), numberOne!!.toDouble()))
                    } else {
                        temp = Result?.plus(numberOne!!.toDouble())
                    }
                }
                "-" -> {
                    if (power_present as Boolean) {
                        temp = Result?.minus(Math.pow(numberTwo!!.toDouble(), numberOne!!.toDouble()))
                    } else {
                        temp = Result?.minus(numberOne!!.toDouble())
                    }
                }
                "x" -> {
                    if (power_present as Boolean) {
                        temp = Result?.times(Math.pow(numberTwo!!.toDouble(), numberOne!!.toDouble()))
                    } else {
                        temp = Result?.times(numberOne!!.toDouble())
                    }
                }
                "/" -> {

                    try {
                        if (power_present as Boolean) {
                            temp = Result?.div(Math.pow(numberTwo!!.toDouble(), numberOne!!.toDouble()))
                        } else {
                            temp = Result?.div(numberOne!!.toDouble())
                        }
                    } catch (e: Exception) {

                        sAnswer = e.message.toString()
                    }

                }

            }

            sAnswer = format?.format(temp).toString();
            updateCalculation()

        }
    }

    //-----------------OPERATOR--------------------

    fun onClickOprator(view: View){

        var op = view as Button

        //if sAnswer is null means no calculation needed
        if (sAnswer != ""){

            //we check last char is operator or not
            if (current_oprator != ""){

                var c:Char = getcharfromLast(sCalculation, 2);// 2 is the char from last because our las char is " "
                if (c == '+' || c == '-' || c == 'x' || c == '/') {
                    sCalculation = sCalculation.substring(0, sCalculation.length - 3);
                }
            }

            sCalculation = sCalculation + "\n" + op.getText() + " ";
            number_one = "";
            Result = temp;
            current_oprator = op.getText().toString();
            updateCalculation();
            //when operator click dot is not present in number_one
            number_two = "";
            numberTwo = 0.0;
            dot_present = false;
            number_allow = true;
            root_present = false;
            invert_allow = true;
            power_present = false;
            factorial_present = false;
            constant_present = false;
            function_present = false;
            value_inverted = false;
        }
    }

    fun getcharfromLast(s: String, i: Int): Char {
        return s[s.length - i]
    }


    fun onClickClear(view: View){

        cleardata();
    }

    private fun cleardata() {

        sCalculation = "";
        sAnswer = "";
        current_oprator = "";
        number_one = "";
        number_two = "";
        prev_ans = "";
        Result = 0.0;
        numberOne = 0.0;
        numberTwo = 0.0;
        temp = 0.0;

        updateCalculation();

        dot_present = false;
        number_allow = true;
        root_present = false;
        invert_allow = true;
        power_present = false;
        factorial_present = false;
        function_present = false;
        constant_present = false;
        value_inverted = false;
    }


    fun updateCalculation() {
        calculation?.text = sCalculation;
        answer?.text = sAnswer;
    }



    fun onDotClick(view: View){

        //create boolean dot_present check if dot is present or not.

        if (!(dot_present as Boolean)) {

            //check length of numberone
            if (number_one.length == 0) {
                number_one = "0.";
                sCalculation += "0.";
                sAnswer = "0.";
                dot_present = true;

                updateCalculation();

            } else {
                number_one += ".";
                sCalculation += ".";
                sAnswer += ".";
                dot_present = true;

                updateCalculation();
            }
        }

    }

    fun onClickEqual(view: View){
        showresult();
    }

    public fun showresult() {

        if (sAnswer != "" && sAnswer != prev_ans) {
            sCalculation += "\n= " + sAnswer + "\n----------\n" + sAnswer + " ";
            number_one = "";
            number_two = "";
            numberTwo = 0.0;
            numberOne = 0.0;
            Result = temp;
            prev_ans = sAnswer;

            updateCalculation();

            //we  don't allow to edit our ans so
            dot_present = true;
            power_present = false;
            number_allow = false;
            factorial_present = false;
            constant_present = false;
            function_present = false;
            value_inverted = false;
        }

    }


    fun onRootClick(view: View){


        var root = view as Button

        //first we check if root is present or not
        if (sAnswer == "" && Result?.equals(0) as Boolean && !(root_present as Boolean) && !(function_present as Boolean)) {
            sCalculation = root.getText().toString();
            root_present = true;
            invert_allow = false;

            updateCalculation();

        } else if (getcharfromLast(sCalculation, 1) == ' ' && current_oprator != "" && !(root_present as Boolean))
        {
            sCalculation += root.getText().toString();
            root_present = true;
            invert_allow = false;

            updateCalculation();
        }
    }

    fun onPowerClick(view: View){

        var power = view as Button

        if (sCalculation != "" && !(root_present as Boolean) && !(power_present as Boolean) && !(function_present as Boolean)) {
            if (getcharfromLast(sCalculation, 1) != ' ') {
                sCalculation += power.getText().toString();
                //we need second variable for the power
                number_two = number_one;
                numberTwo = numberOne;
                number_one = "";
                power_present = true;
                updateCalculation();
            }
        }
    }
    fun onSquareClick(view: View){

        if (sCalculation != "" && sAnswer != "") {

            if (!(root_present!!) && !(function_present!!) && !power_present!! && getcharfromLast(sCalculation, 1) != ' ' && getcharfromLast(sCalculation, 1) != ' ') {
                numberOne = (numberOne?.times(numberOne as Double))
                number_one = format?.format(numberOne).toString();

                if (current_oprator == "") {
                    if (number_one.length > 9) {
                        number_one = longformate!!.format(numberOne);
                    }
                    sCalculation = number_one;
                    temp = numberOne;
                } else {
                    when(current_oprator){

                        "+" ->{
                            temp = Result?.plus(numberOne as Double)
                        }
                        "-" ->{
                            temp = Result?.minus(numberOne as Double);
                        }
                        "*" ->{
                        temp = Result?.times(numberOne as Double);
                         }
                        "/" ->{
                            try {
                                temp = Result?.minus(numberOne as Double);
                            }catch (e:Exception){
                                e.printStackTrace()
                            }

                        }
                    }

                    removeuntilchar(sCalculation, ' ');
                    if (number_one.length > 9) {
                        number_one = longformate!!.format(numberOne);
                    }
                    sCalculation += number_one;
                }
                sAnswer = format!!.format(temp);
                if (sAnswer.length> 9) {
                    sAnswer = longformate!!.format(temp);
                }
                updateCalculation();

            }
        }

    }

    private fun removeuntilchar(sCalculation: String, c: Char) {

    }

    fun onClickFactorial(view: View){

        
    }
    fun onClickInverse(view: View){

    }
    fun onClickPIorE(view: View){

    }
    fun onClickFunction(view: View){

    }

    fun onPorMClick(view: View){

    }
    fun onModuloClick(view: View){

    }



    fun onClickDelete(view: View){

    }





    fun calculateFunction(function:String?){

    }
}
