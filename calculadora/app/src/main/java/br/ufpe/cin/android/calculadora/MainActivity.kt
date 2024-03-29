package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //super.onSaveInstanceState(savedInstanceState);

        // carregando valores salvos nos campos
        if (savedInstanceState != null) {
            text_info.setText(savedInstanceState.getCharSequence("calc_state"))
        }



        // Setando os listeners dos botoes existentes no layout
    // acessando o campo de texto e adicionando o digito clicado
        btn_0.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '0')
        }

        btn_1.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '1')
        }

        btn_2.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '2')
        }

        btn_3.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '3')
        }

        btn_4.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '4')
        }

        btn_5.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '5')
        }

        btn_6.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '6')
        }

        btn_7.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '7')
        }

        btn_8.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '8')
        }

        btn_9.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '9')
        }


        btn_LParen.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '(')
        }

        btn_RParen.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + ')')
        }

        btn_Add.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '+')
        }

        btn_Subtract.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '-')
        }

        btn_Multiply.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '*')
        }

        btn_Divide.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '/')
        }

        btn_Dot.setOnClickListener {
            val textField = text_calc.text.toString()
            text_calc.setText(textField + '.')
        }

        btn_Clear.setOnClickListener {
            text_calc.setText("")
        }

        // tenta valorar a expressao encontrada no campo de texto da calculadora e atualizar o campo com o novo valor digitado
        // em caso de falha na tentativa de valoracao, um toast é acionado informando o erro
        btn_Equal.setOnClickListener {
            try {
                val expr = eval(text_calc.text.toString()).toString()
                text_calc.setText(expr)

                text_info.setText(text_calc.text)
            } catch (e: java.lang.RuntimeException) {
                print("some error")
                text_calc.setText("ERROR!")
                Toast.makeText(this, "Wrong Expression.", Toast.LENGTH_SHORT).show()
            }
        }



    }

    // metodo para salvar estado da calculadora
    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        //super call recommended
        super.onSaveInstanceState(savedInstanceState)

        // salvo o estado do resultado da calculadora para restaurar no onCreate
        savedInstanceState?.putCharSequence("calc_state", text_info.text)
    }


    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch.toChar())
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
