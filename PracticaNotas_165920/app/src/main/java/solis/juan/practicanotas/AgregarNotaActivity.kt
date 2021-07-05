package solis.juan.practicanotas

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class AgregarNotaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)

        val btn_guardar = findViewById<Button>(R.id.btn_guardar)
        btn_guardar.setOnClickListener {
            guardar_nota()
        }
    }

    fun guardar_nota() {
        //Verifica que tena los permisos
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Si no los tiene, los pide al usuario
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 235)
        } else {
            guardar()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            235 -> {
                //Pregunta si el usuario acepto los permisos
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    guardar()
                } else {
                    Toast.makeText(this, "Error: permisos denegados", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    public fun guardar() {
        var titulo = findViewById<EditText>(R.id.et_titulo).text.toString()
        var cuerpo = findViewById<EditText>(R.id.et_contenido).text.toString()

        if (titulo == "" || cuerpo == "") {
            Toast.makeText(this, "Error: campos vacios", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val archivo = File(ubicacion(), titulo + ".txt")
                val fos = FileOutputStream(archivo)
                fos.write(cuerpo.toByteArray())
                fos.close()

                Toast.makeText(this, "Se guardo el archivo en la carpeta publica", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Error: no se guardo el archivo", Toast.LENGTH_SHORT).show()
            }
        }

        finish()
    }

    private fun ubicacion(): String {
        val carpeta = File(getExternalFilesDir(null), "notas")

        if (!carpeta.exists()) {
            carpeta.mkdir()
        }

        return carpeta.absolutePath
    }
}