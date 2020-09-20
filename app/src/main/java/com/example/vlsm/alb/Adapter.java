package com.example.vlsm.alb;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.vlsm.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class Adapter extends SelectableAdapter<Adapter.ViewHolder> {
	@SuppressWarnings("unused")
	private static final String TAG = Adapter.class.getSimpleName();

	private static final int TYPE_INACTIVE = 0;
	private static final int TYPE_ACTIVE = 1;

	private static final int ITEM_COUNT = 19;
	private List<Item> items;

	private ViewHolder.ClickListener clickListener;

	public Adapter(ViewHolder.ClickListener clickListener) {
		super();

		this.clickListener = clickListener;

		// Create some items
		Random random = new Random();
		//PersistenciaTareas persistenciaTareas =  PersistenciaTareas.getConfiguracion();
		items = new ArrayList<>();//persistenciaTareas.getItems();
	/*	items = persistenciaTareas.getItems();*/
		for (int i = 0; i < ITEM_COUNT; ++i) {
			items.add(new Item("Item " + i, "This is the item number " + i, random.nextBoolean()));
		}


	}

	public void removeItem(int position) {
		items.remove(position);
		notifyItemRemoved(position);

	}



	public void removeItems(List<Integer> positions) {
		// Reverse-sort the list
		Collections.sort(positions, new Comparator<Integer>() {
			@Override
			public int compare(Integer lhs, Integer rhs) {
				return rhs - lhs;
			}
		});//los ordena de mayor a menor, (arriba a abajo), para que no se altere la position, o sea, si se borra el 1, entonces el 2, sería el nuevo 1
		//en cambio si se borra el 6 primero, no afectaria.

		// Split the list in ranges
		while (!positions.isEmpty()) {
			if (positions.size() == 1) {
				removeItem(positions.get(0));
				positions.remove(0);
			} else {
				int count = 1;
				while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
					++count;
				}//creo es para detectar un removeRange
//posicion count, es igual a posicion count - 1
				if (count == 1) {//si la cuenta es 1, elimina solo ese item//si no, llama al removeRange
					removeItem(positions.get(0));
				} else {
					removeRange(positions.get(count - 1), count);
				}

				for (int i = 0; i < count; ++i) {
					positions.remove(0);//remueve el (los) elementos de la lista posiciones.Siempre remueve 0, o sea, el top.
				}
			}
		}
	}

	private void removeRange(int positionStart, int itemCount) {
		for (int i = 0; i < itemCount; ++i) {
			items.remove(positionStart);
		}
		notifyItemRangeRemoved(positionStart, itemCount);
	}
	public Item getItemAt(int position){
		return items.get(position);
	}

	public void updateItem(int position){
		notifyItemChanged(position);
	}

	public void addItem(Item item){
		items.add( 0,item);
		//notifyItemInserted(items.size()-1);
		notifyItemInserted(0);

	}

	public void guardarTareas(){
/*
		PersistenciaTareas.setConfiguracion(new PersistenciaTareas(this.items));
*/
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		final int layout = viewType == TYPE_INACTIVE ? R.layout.item : R.layout.item_active;

		View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
		return new ViewHolder(v, clickListener);
	}

	public void marcarTareasHechas(List<Integer> positions) {

		Collections.sort(positions, new Comparator<Integer>() {
			@Override
			public int compare(Integer lhs, Integer rhs) {
				return rhs - lhs;
			}
		});

		while(!positions.isEmpty()){
			if (positions.size() == 1) {
				marcarTareaHecha(positions.get(0));
				positions.remove(0);
			} else {
				int count = 1;
				while (positions.size() > count && positions.get(count).equals(positions.get(count - 1) - 1)) {
					++count;
				}//detecta un rango de seleccion, para hacerlo al mismo tiempo.

				if (count == 1) {//si la cuenta es 1, elimina solo ese item//si no, llama al removeRange
					marcarTareaHecha(positions.get(0));
				} else {
					marcarRangoTareasHechas(positions.get(count - 1), count);
				}

				for (int i = 0; i < count; ++i) {
					positions.remove(0);//remueve el (los) elementos de la lista posiciones.Siempre remueve 0, o sea, el top.
				}
			}
		}


	}
	private void marcarTareaHecha(int position){
		items.get(position).setActive(true);
		notifyItemChanged(position);
	}

	private void marcarRangoTareasHechas(int positionStart,int itemCount){
		for (int i = 0; i < itemCount; ++i) {
			items.get(positionStart+i).setActive(true);
		}
		notifyItemRangeChanged(positionStart, itemCount);
	}
	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final Item item = items.get(position);

		holder.title.setText(item.getTitle());
		holder.subtitle.setText(item.getSubtitle() + ", which is " + (item.isActive() ? "active" : "inactive"));

		// Span the item if active
		final ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
		//aquí cambia la forma del viewholder, si es cuadrado y rectangulo
		if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
			StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) lp;
			sglp.setFullSpan(item.isActive());
			holder.itemView.setLayoutParams(sglp);
		}

		// Highlight the item if it's selected
		holder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public int getItemViewType(int position) {
		final Item item = items.get(position);

		return item.isActive() ? TYPE_ACTIVE : TYPE_INACTIVE;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
			View.OnLongClickListener {
		@SuppressWarnings("unused")
		private static final String TAG = ViewHolder.class.getSimpleName();

		TextView title;
		TextView subtitle;
		View selectedOverlay;

		private ClickListener listener;

		public ViewHolder(View itemView, ClickListener listener) {
			super(itemView);

			title = (TextView) itemView.findViewById(R.id.title);
			subtitle = (TextView) itemView.findViewById(R.id.subtitle);
			selectedOverlay = itemView.findViewById(R.id.selected_overlay);

			this.listener = listener;

			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (listener != null) {
				listener.onItemClicked(getLayoutPosition());
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (listener != null) {
				return listener.onItemLongClicked(getLayoutPosition());
			}

			return false;
		}

		public interface ClickListener {
			 void onItemClicked(int position);
			 boolean onItemLongClicked(int position);
		}
	}
}
