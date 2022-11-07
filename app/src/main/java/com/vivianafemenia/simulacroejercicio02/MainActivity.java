package com.vivianafemenia.simulacroejercicio02;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vivianafemenia.simulacroejercicio02.adapters.ProductosAdapter;
import com.vivianafemenia.simulacroejercicio02.databinding.ActivityMainBinding;
import com.vivianafemenia.simulacroejercicio02.modelo.Producto;

import java.text.NumberFormat;
import java.util.ArrayList;

/*Ejercicio 02
Desarrollar un programa nos permita hacer un CRUD de la lista de la compra. (BASIC)
Cada producto tendrá:
- Nombre
- Precio
- Cantidad
La MainActivity tendrá en la parte superior dos TextViews que nos mostrará la cantidad de productos
que tiene la lista y el otro el importe total de la lista en formato numérico (moneda) según la
configuración de idioma del terminal.Debajo de los TextViews tendremos un RecyclerView donde veremos
los datos de cada producto, nombre, cantidad y precio.
Para crear un producto, tendremos una segunda activity con 3 EditText uno para el nombre, otro para
la cantidad y otro para el precio. Junto con un botón de agregar al carrito.
Finalmente, no será necesario crear una actividad para modificar el producto, y la eliminación de
este se hará con un botón. al presionar corto cualquiera de las filas del Recycler, muestre un
AlertDialog para poder modificar el elemento de la fila, pero sólo Cantidad y Precio.
Proteger la eliminación de elementos de modo que se pida al usuario con un AlertDialog si se quiere eliminar el elemento.


 */

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding;
    private ArrayList<Producto> productosList;
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        productosList= new ArrayList<>();

        adapter = new ProductosAdapter(productosList, R.layout.producto_view_holder, this);
        layoutManager = new GridLayoutManager(this, 1);

        binding.contentMain.contenedor.setLayoutManager(layoutManager);
        binding.contentMain.contenedor.setAdapter(adapter);




        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto();
            }
        });
    }

    private AlertDialog createProducto() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Introduce un Producto");
        builder.setCancelable(false);

        View productoViewModel = LayoutInflater.from(this).inflate(R.layout.producto_view_model, null);

        EditText txtNombre = productoViewModel.findViewById(R.id.txtNombreProductoViewModel);
        EditText txtCantidad = productoViewModel.findViewById(R.id.txtCantidadProductoViewModel);
        EditText txtPrecio = productoViewModel.findViewById(R.id.txtPrecioProductoViewModel);
        Button btnAgregar = productoViewModel.findViewById(R.id.btnAgregarProductoViewModel);

        builder.setView(productoViewModel);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("EVENTO_TEXTO", "BEFORE "+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("EVENTO_TEXTO", "ONCHANGED "+charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("EVENTO_TEXTO", "AFTER "+editable.toString());
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float precio = Float.parseFloat(txtPrecio.getText().toString());

                    float total = cantidad * precio;
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    //txtTotal.setText(numberFormat.format(total));
                }
                catch (NumberFormatException nfe) {}
            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setNegativeButton(R.string.alert_cancel_button, null);
        builder.setPositiveButton(R.string.alert_add_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() &&
                        !txtPrecio.getText().toString().isEmpty()) {
                    Producto producto = new Producto(
                            txtNombre.getText().toString(),
                            Integer.parseInt(txtCantidad.getText().toString()),
                            Float.parseFloat(txtPrecio.getText().toString())
                    );

                    productosList.add(0, producto);
                    //adapter.notifyItemInserted(0);

                }
                else {
                    Toast.makeText(MainActivity.this, "Faltan Datos", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Lista",productosList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        productosList.addAll((ArrayList<Producto>) savedInstanceState.getSerializable("Lista"));
        adapter.notifyItemRangeInserted(0,productosList.size());
    }

    }


