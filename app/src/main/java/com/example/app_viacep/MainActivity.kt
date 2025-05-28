package com.example.app_viacep

import android.graphics.Color
import android.os.Binder
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import api.Api
import com.example.app_viacep.databinding.ActivityMainBinding
import model.Endereco
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding. inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#21618C")

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.BuscarCep.setOnClickListener{
            val cep = binding.EditCep.text.toString()
            if (cep.isEmpty()){
                Toast.makeText(this, "Preencher o campo CEP", Toast.LENGTH_SHORT).show()

            }else {

                retrofit.setEndereco(cep).enqueue(object : Callback<Endereco>{
                    override fun onResponse(p0: Call<Endereco>, response: Response<Endereco>) {
                        if(response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf = response.body()?.uf.toString()
                            setFormulario(logradouro, bairro, localidade, uf)
                        }
                        else{

                            Toast.makeText(applicationContext, "CEP Errado",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(p0: Call<Endereco>, p1: Throwable) {
                        Toast.makeText(applicationContext, "Erro no servidor",Toast.LENGTH_SHORT).show()
                    }

                }
                )
            }
        }
        binding.Limpar.setOnClickListener {
            clearFormulario()
        }

    }

    private fun setFormulario(logradouro: String, bairro: String, localidade: String, uf: String ){
        binding.EditLogradouro.setText(logradouro)
        binding.EditBairro.setText(bairro)
        binding.EditLocalidade.setText(localidade)
        binding.EditUf.setText(uf)

    }
    private fun clearFormulario() {
        binding.EditCep.setText("")
        binding.EditLogradouro.setText("")
        binding.EditBairro.setText("")
        binding.EditLocalidade.setText("")
        binding.EditUf.setText("")
    }

}