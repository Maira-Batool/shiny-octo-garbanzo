package com.example.scientific_calc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Stack;
import java.util.Random;
import java.lang.Math;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDisplay;
    private String memoryValue = "0";
    private boolean isNewOp = true;
    private boolean isRad = true;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_horizontal);
        } else {
            setContentView(R.layout.activity_main);
        }

        editTextDisplay = findViewById(R.id.editTextDisplay);

        int[] numButtons = {R.id.bt1,R.id.bt2,R.id.bt3,R.id.bt4,R.id.bt5,R.id.bt6,R.id.bt7,R.id.bt8,R.id.bt9,R.id.btZ};
        for(int id : numButtons){
            findViewById(id).setOnClickListener(numListener);
        }

        findViewById(R.id.btDecimal).setOnClickListener(v -> {
            if(isNewOp){
                editTextDisplay.setText("0");
                isNewOp = false;
            }
            if(!editTextDisplay.getText().toString().contains(".")) editTextDisplay.append(".");
        });

        int[] opButtons = {R.id.Addbt, R.id.subtractionbt, R.id.Multiplybt, R.id.dividebt, R.id.xybt};
        for(int id: opButtons) findViewById(id).setOnClickListener(opListener);

        findViewById(R.id.btE).setOnClickListener(v -> calculate());

        findViewById(R.id.Clear).setOnClickListener(v -> {
            editTextDisplay.setText("");
            isNewOp = true;
        });

        findViewById(R.id.btdel).setOnClickListener(v -> {
            String text = editTextDisplay.getText().toString();
            if(!text.isEmpty()) editTextDisplay.setText(text.substring(0,text.length()-1));
        });

        findViewById(R.id.openingBT).setOnClickListener(v -> editTextDisplay.append("("));
        findViewById(R.id.closingBT).setOnClickListener(v -> editTextDisplay.append(")"));

        findViewById(R.id.bt).setOnClickListener(v -> {
            String val = editTextDisplay.getText().toString();
            if(!val.isEmpty()){
                if(val.startsWith("-")) editTextDisplay.setText(val.substring(1));
                else editTextDisplay.setText("-" + val);
            }
        });

        findViewById(R.id.mcbt).setOnClickListener(v -> memoryValue = "0");
        findViewById(R.id.mrbt).setOnClickListener(v -> editTextDisplay.setText(memoryValue));
        findViewById(R.id.maddbt).setOnClickListener(v -> memoryValue = removeTrailingZero(evaluateMemory(memoryValue, editTextDisplay.getText().toString(), '+')));
        findViewById(R.id.msubbt).setOnClickListener(v -> memoryValue = removeTrailingZero(evaluateMemory(memoryValue, editTextDisplay.getText().toString(), '-')));

        findViewById(R.id.Radbt).setOnClickListener(v -> isRad = !isRad);

        findViewById(R.id.sinbt).setOnClickListener(v -> applyFunction("sin"));
        findViewById(R.id.cosbt).setOnClickListener(v -> applyFunction("cos"));
        findViewById(R.id.tanbt).setOnClickListener(v -> applyFunction("tan"));
        findViewById(R.id.sinhbt).setOnClickListener(v -> applyFunction("sinh"));
        findViewById(R.id.coshbt).setOnClickListener(v -> applyFunction("cosh"));
        findViewById(R.id.tanhbt).setOnClickListener(v -> applyFunction("tanh"));
        findViewById(R.id.logbt).setOnClickListener(v -> applyFunction("log"));
        findViewById(R.id.expIN).setOnClickListener(v -> applyFunction("ln"));
        findViewById(R.id.xbt).setOnClickListener(v -> applyFunction("fact"));
        findViewById(R.id.pibt).setOnClickListener(v -> editTextDisplay.setText(removeTrailingZero(Math.PI)));
        findViewById(R.id.ebt).setOnClickListener(v -> editTextDisplay.setText(removeTrailingZero(Math.E)));
        findViewById(R.id.Randt).setOnClickListener(v -> editTextDisplay.setText(removeTrailingZero(rand.nextDouble())));
        findViewById(R.id.Tenxbt).setOnClickListener(v -> applyFunction("10^x"));
        findViewById(R.id.expbt).setOnClickListener(v -> applyFunction("e^x"));
        findViewById(R.id.xtwobt).setOnClickListener(v -> applyFunction("x^2"));
        findViewById(R.id.xthreebt).setOnClickListener(v -> applyFunction("x^3"));
        findViewById(R.id.onedividedbyxbt).setOnClickListener(v -> applyFunction("1/x"));
        findViewById(R.id.radicalttwobt).setOnClickListener(v -> applyFunction("sqrt"));
        findViewById(R.id.radicalhreebt).setOnClickListener(v -> applyFunction("cbrt"));
    }

    private View.OnClickListener numListener = v -> {
        Button btn = (Button)v;
        if(isNewOp){
            editTextDisplay.setText("");
            isNewOp = false;
        }
        editTextDisplay.append(btn.getText());
    };

    private View.OnClickListener opListener = v -> {
        Button btn = (Button)v;
        String text = editTextDisplay.getText().toString();
        if(!text.isEmpty() && !endsWithOperator(text)){
            editTextDisplay.append(btn.getText().toString());
            isNewOp = false;
        } else if(!text.isEmpty()) {
            editTextDisplay.setText(text.substring(0,text.length()-1)+btn.getText().toString());
        }
    };

    private boolean endsWithOperator(String s){
        return s.endsWith("+")||s.endsWith("-")||s.endsWith("x")||s.endsWith("÷")||s.endsWith("^");
    }

    private double evaluateMemory(String a, String b, char op){
        double x = Double.parseDouble(a);
        double y = Double.parseDouble(b);
        return op=='+' ? x+y : x-y;
    }

    private void applyFunction(String func){
        try{
            double val = Double.parseDouble(editTextDisplay.getText().toString());
            double res=0;
            switch(func){
                case "sin": res = isRad? Math.sin(val) : Math.sin(Math.toRadians(val)); break;
                case "cos": res = isRad? Math.cos(val) : Math.cos(Math.toRadians(val)); break;
                case "tan": res = isRad? Math.tan(val) : Math.tan(Math.toRadians(val)); break;
                case "sinh": res = Math.sinh(val); break;
                case "cosh": res = Math.cosh(val); break;
                case "tanh": res = Math.tanh(val); break;
                case "ln": if(val<=0) throw new ArithmeticException(); res = Math.log(val); break;
                case "log": if(val<=0) throw new ArithmeticException(); res = Math.log10(val); break;
                case "fact": if(val<0||val!=(int)val) throw new ArithmeticException(); res = factorial((int)val); break;
                case "10^x": res = Math.pow(10,val); break;
                case "e^x": res = Math.exp(val); break;
                case "x^2": res = val*val; break;
                case "x^3": res = val*val*val; break;
                case "1/x": if(val==0) throw new ArithmeticException(); res = 1/val; break;
                case "sqrt": if(val<0) throw new ArithmeticException(); res = Math.sqrt(val); break;
                case "cbrt": res = Math.cbrt(val); break;
            }
            editTextDisplay.setText(removeTrailingZero(res));
            isNewOp = true;
        }catch(Exception e){
            editTextDisplay.setText("Error");
            isNewOp = true;
        }
    }

    private long factorial(int n){
        long f=1;
        for(int i=1;i<=n;i++) f*=i;
        return f;
    }

    private String removeTrailingZero(double val){
        if(val==(long)val) return String.valueOf((long)val);
        else return String.valueOf(val);
    }

    private void calculate(){
        String expr = editTextDisplay.getText().toString();
        try{
            double result = evaluate(expr);
            editTextDisplay.setText(removeTrailingZero(result));
        }catch(Exception e){
            editTextDisplay.setText("Error");
        }
        isNewOp = true;
    }

    private double evaluate(String expr){
        Stack<Double> nums = new Stack<>();
        Stack<Character> ops = new Stack<>();
        expr = expr.replaceAll(" ","");
        int i=0;
        while(i<expr.length()){
            char c = expr.charAt(i);
            if(Character.isDigit(c) || c=='.'){
                StringBuilder sb = new StringBuilder();
                while(i<expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i)=='.')) sb.append(expr.charAt(i++));
                nums.push(Double.parseDouble(sb.toString()));
                continue;
            } else if(c=='('){
                ops.push(c);
            } else if(c==')'){
                while(ops.peek()!='(') nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
                ops.pop();
            } else if(isOperator(c)){
                while(!ops.isEmpty() && precedence(ops.peek())>=precedence(c))
                    nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
                ops.push(c);
            }
            i++;
        }
        while(!ops.isEmpty())
            nums.push(applyOp(ops.pop(), nums.pop(), nums.pop()));
        return nums.pop();
    }

    private boolean isOperator(char c){
        return c=='+'||c=='-'||c=='x'||c=='÷'||c=='^';
    }

    private int precedence(char op){
        switch(op){
            case '+': case '-': return 1;
            case 'x': case '÷': return 2;
            case '^': return 3;
        }
        return 0;
    }

    private double applyOp(char op, double b, double a){
        switch(op){
            case '+': return a+b;
            case '-': return a-b;
            case 'x': return a*b;
            case '÷': if(b==0) throw new ArithmeticException(); return a/b;
            case '^': return Math.pow(a,b);
        }
        return 0;
    }
}
