package com.vivianafemenia.simulacroejercicio02.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.vivianafemenia.simulacroejercicio02.R;
import com.vivianafemenia.simulacroejercicio02.modelo.Producto;

import java.text.NumberFormat;
import java.util.List;

public class ProductosAdapter  extends RecyclerView.Adapter<ProductosAdapter.ProductoVH> {

    private List<Producto> objects;
    private int fila;
    private Context context;

    public ProductosAdapter(List<Producto> objects, int fila, Context context) {
        this.objects = objects;
        this.fila = fila;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductosAdapter.ProductoVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productoView = LayoutInflater.from(context).inflate(fila, null);
        productoView.setLayoutParams(
                new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );
        return new ProductoVH(productoView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosAdapter.ProductoVH holder, int position) {
        Producto producto = objects.get(position);
        holder.txtCantidad.setText(String.valueOf(producto.getCantidad()));
        holder.txtTotal.setText(String.valueOf(producto.getTotal()));
        holder.txtCantidad.addTextChangedListener(new TextWatcher() {
            boolean cero =false;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()> 0 && charSequence.charAt(0) == '0')
                    cero=true;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try{
                    if(cero && editable.toString().length()> 1){
                        holder.txtCantidad.setText(editable.toString().substring(0,1));
                        holder.txtCantidad.setSelection(1);
                        cero=false;
                    }

                    int cantidad = Integer.parseInt(editable.toString());
                    producto.setCantidad(cantidad);
                    //notifyItemChanged(holder.getAdapterPosition());
                }
                catch (NumberFormatException numberFormatException){
                    holder.txtCantidad.setText("0");
                }
            }
        });
 holder.itemView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         UpdateProducto(producto).show();
     }
 });@Override
        public int getItemCount() {
            return objects.size();
        }
        private androidx.appcompat.app.AlertDialog UpdateProducto(Producto producto) {
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle("Editar Producto de la cesta");
            builder.setCancelable(false);

            View productoViewModel = LayoutInflater.from(context).inflate(R.layout.producto_view_model, null);
            TextView lblTotal = productoViewModel.findViewById(R.id.txtTotalProductoHolder);
            EditText txtNombre = productoViewModel.findViewById(R.id.txtNombreProductoViewModel);
            EditText txtCantidad = productoViewModel.findViewById(R.id.txtCantidadProductoViewModel);
            EditText txtPrecio = productoViewModel.findViewById(R.id.txtPrecioProductoViewModel);



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
                        lblTotal.setText(numberFormat.format(total));
                    }
                    catch (NumberFormatException nfe) {}
                }
            };

            txtCantidad.addTextChangedListener(textWatcher);
            txtPrecio.addTextChangedListener(textWatcher);

            txtCantidad.setText(String.valueOf(producto.getCantidad()));
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtNombre.setText(producto.getNombre());

            builder.setNegativeButton("CANCELAR", null);
            builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() &&
                            !txtPrecio.getText().toString().isEmpty()) {

                        producto.setCantidad(Integer.parseInt(txtCantidad.getText().toString()));
                        producto.setPrecio(Float.parseFloat(txtPrecio.getText().toString()));
                        producto.setNombre(txtNombre.getText().toString());
                        producto.actualizaTotal();

                        notifyItemChanged(objects.indexOf(producto));
                    }
                    else {
                        Toast.makeText(context, "Faltan Datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            builder.create();
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public class ProductoVH extends RecyclerView.ViewHolder {

        EditText txtTotal;
        EditText txtCantidad;


        public ProductoVH(@NonNull View itemView) {
            super(itemView);
            txtTotal = itemView.findViewById(R.id.txtTotalProductoHolder);
            txtCantidad = itemView.findViewById(R.id.txtCantidadProductoHolder);

        }
    }
    private AlertDialog confirmDelete(Producto producto){
        AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("Segurooooo");
        builder.setCancelable(false);
        TextView textView=new TextView(context);
        textView.setText(R.string.alert_aviso);
        textView.setTextColor(Color.RED);
        textView.setTextSize(24);
        textView.setPadding(125,25,25,25);

        builder.setView(textView);
        builder.setNegativeButton("Me arrepiento",null);
        builder.setPositiveButton("Con dos cojones", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int posicion=objects.indexOf(producto);
                objects.remove(producto);
                notifyItemRemoved(posicion);
            }
        });
        return builder.create();
    }
}
