package com.example.inventoryapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.avilable_goods.model.AvaialbleGoodsModel;
import com.example.inventoryapp.avilable_goods.model.AvilableGoods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewModelAdapter extends RecyclerView.Adapter<RecyclerViewModelAdapter.ViewHolder> {
   static List<AvaialbleGoodsModel> avaialbleGoodsModelList =new ArrayList<>();
    //AvilableGoods a=new AvilableGoods();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mListener=onItemClickListener;
    }

    public RecyclerViewModelAdapter(List<AvaialbleGoodsModel> avaialbleGoodsModelList) {
        this.avaialbleGoodsModelList = avaialbleGoodsModelList;
    }

    @NonNull
    @Override
    public RecyclerViewModelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avilable_products,parent,false);
        return new ViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewModelAdapter.ViewHolder holder, int position) {
        String productName=avaialbleGoodsModelList.get(position).getPrductName();
        String purchaseDate=avaialbleGoodsModelList.get(position).getPurchaseDate();
        int purchasePrice=avaialbleGoodsModelList.get(position).getPurchasePrice();
        int salesPrice=avaialbleGoodsModelList.get(position).getSalesPrice();
        int quantity=avaialbleGoodsModelList.get(position).getQuantity();
        String image=avaialbleGoodsModelList.get(position).getProductImage();
        holder.setData(productName,purchaseDate,purchasePrice,salesPrice,quantity,image);


    }

    @Override
    public int getItemCount() {
        return avaialbleGoodsModelList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pName,purhcasePrice2,salesPrice2,date2,quantity2;
        private ImageView productImage;
        public ViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            pName=itemView.findViewById(R.id.aProductname);
            purhcasePrice2=itemView.findViewById(R.id.purchasePrice2);
            salesPrice2=itemView.findViewById(R.id.salePrice);
            date2=itemView.findViewById(R.id.date);
            quantity2=itemView.findViewById(R.id._quantity);
            productImage=itemView.findViewById(R.id.productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        int position=getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }


        public  void setData(String productName,String purchaseDate,int purchasePrice,int salesPrice,int quantity,String image){
            try {
                pName.setText(productName);
                salesPrice2.setText(Integer.toString(salesPrice));
                purhcasePrice2.setText(Integer.toString(purchasePrice));
                date2.setText(purchaseDate);
                quantity2.setText(Integer.toString(quantity));
                Glide.with(itemView.getContext()).load(new File(image).getPath()).into(productImage);
                //notifyDataSetChanged();
            }
            catch (Exception ex){
                Log.d(""+ex,"errorsdfassadfdsfasdsdfs");
            }
        }

    }
    public void updateList(List<AvaialbleGoodsModel> newList){
        avaialbleGoodsModelList=new ArrayList<>();
        avaialbleGoodsModelList.addAll(newList);
        notifyDataSetChanged();
    }


}
