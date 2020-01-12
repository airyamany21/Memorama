package com.example.memorama;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final int[] CARTA_RESOURCES = new int[]{

            // se ponen las fotos (Frozen) para el memorama
            R.drawable.anna,
            R.drawable.elsa,
            R.drawable.kristoff,
            R.drawable.olaf,
            R.drawable.personajes,
            R.drawable.sven

    };

    private final Handler handler = new Handler();
    private Carta[] cartas;
    private boolean touchActivo = true;
    private Carta visible = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TableLayout table = new TableLayout(this);
        final int tam=4;
        final int tam2=3;
        //cartas = crearCeldas(tam*tam2);
        Collections.shuffle(Arrays.asList(cartas));
        for (int y=0; y<tam; y++){
            final TableRow fila = new TableRow(this);
            for (int x=0; x<tam2; x++){
                fila.addView(cartas[(y*tam)+x].boton);
            }
            table.addView(fila);
        }
        setContentView(table);
    }

    private class Carta implements View.OnClickListener {

        private final ImageButton boton;
        private final int imagen;
        private boolean caraVisible = false;

        Carta(final int imagen) {

            this.imagen = imagen;
            this.boton = new ImageButton(MainActivity.this);
            this.boton.setLayoutParams(new TableRow.LayoutParams(64, 64));
            this.boton.setScaleType(ImageView.ScaleType.FIT_XY);
            this.boton.setImageResource(R.drawable.linea);
            this.boton.setOnClickListener(this);
        }

        void setCaraVisible(final boolean caraVisible) {
            this.caraVisible = caraVisible;
            boton.setImageResource(caraVisible ? imagen : R.drawable.linea);
        }

        @Override
        public void onClick(View v) {

            if (!caraVisible && touchActivo) {
                onCartaDescubierta(this);
            }
        }
    }

    private Carta[] crearCeldas(final int cont) {

        final Carta[] array = new Carta[cont];
        for (int i = 0; i < cont; i++) {
            array[i] = new Carta(CARTA_RESOURCES[i / 2]);
        }
        return array;
    }

    public void onCartaDescubierta(final Carta celda) {

        if (visible==null){
            visible=celda;
            visible.setCaraVisible(true);
        }else if(visible.imagen==celda.imagen){
            celda.setCaraVisible(true);
            celda.boton.setEnabled(false);
            visible.boton.setEnabled(false);
            visible=null;
        }else {
            celda.setCaraVisible(true);
            touchActivo=false;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    celda.setCaraVisible(false);
                    visible.setCaraVisible(false);
                    visible = null;
                    touchActivo = true;
                }
            },1000);
        }

    }
}
