package solis.juan.practicanotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class MainActivity : AppCompatActivity() {

    var notas = ArrayList<Nota>()
    lateinit var adaptador: AdaptadorNotas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //notasDePrueba()

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            var intent = Intent(this, AgregarNotaActivity::class.java)
            startActivityForResult(intent, 123)
        }

        adaptador = AdaptadorNotas(this, notas)
        val listview = findViewById<ListView>(R.id.listview)
        listview.adapter = adaptador
    }

    fun notasDePrueba() {
        notas.add(Nota("prueba 1", "contenido de la nota 1"))
        notas.add(Nota("prueba 2", "contenido de la nota 2"))
        notas.add(Nota("prueba 3", "contenido de la nota 3"))
    }

    fun leerNotas() {
        notas.clear()
        var carpeta = File(ubicacion().absolutePath)

        if (carpeta.exists()) {
            var archivos = carpeta.listFiles()

            if (archivos != null) {
                for (archivo in archivos) {
                    leerArchivo(archivo)
                }
            }
        }
    }

    fun leerArchivo(archivo: File) {
        val fis = FileInputStream(archivo)
        val di = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData = ""

        //Lee los archivos almacenados en la memoria
        while (strLine != null) {
            myData = myData +strLine
            strLine = br.readLine()
        }

        br.close()
        di.close()
        fis.close()

        //elimina el .txt
        var nombre = archivo.name.substring(0, archivo.name.length-4)
        //crea la nota y la agrega a la lista
        var nota = Nota(nombre, myData)
        notas.add(nota)
    }

    private fun ubicacion(): File {
        val folder = File(getExternalFilesDir(null), "notas")

        if (!folder.exists()) {
            folder.mkdir()
        }

        return folder
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //se activ cuando regresa del AgregarNotaActivity
        if (requestCode == 123) {
            leerNotas()
            adaptador.notifyDataSetChanged()
        }
    }
}