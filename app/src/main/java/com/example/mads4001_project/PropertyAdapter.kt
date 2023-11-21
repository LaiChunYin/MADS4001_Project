import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mads4001_project.Property
import com.example.mads4001_project.databinding.ItemPropertyBinding

class PropertyAdapter : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    private var properties: MutableList<Property> = mutableListOf()

    fun setProperties(newProperties: List<Property>) {
        val diffResult = DiffUtil.calculateDiff(PropertyDiffCallback(properties, newProperties))
        properties.clear()
        properties.addAll(newProperties)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val binding = ItemPropertyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PropertyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int = properties.size

    class PropertyViewHolder(private val binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(property: Property) {
            binding.propertyAddressTextView.text = property.address
            binding.propertyCityPostalTextView.text = "${property.city}, ${property.postalCode}"
            binding.propertyDescriptionTextView.text = property.description
            Glide.with(binding.root.context)
                .load(property.imageURL)
                .into(binding.propertyImageView)
        }
    }

    private class PropertyDiffCallback(
        private val oldProperties: List<Property>,
        private val newProperties: List<Property>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldProperties.size
        override fun getNewListSize(): Int = newProperties.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldProperties[oldItemPosition] == newProperties[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldProperties[oldItemPosition] == newProperties[newItemPosition]
    }
}
