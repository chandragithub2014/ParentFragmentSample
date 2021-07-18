package colruyt.android.parentfragment.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import colruyt.android.parentfragment.R
import com.google.android.material.appbar.MaterialToolbar

import colruyt.android.parentfragment.viewmodel.InfiniteListViewModel
import colruyt.android.parentfragment.viewmodel.InfiniteViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ParentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ParentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var toolBar: MaterialToolbar
    lateinit var infiniteListViewModel: InfiniteListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolBar  = view.findViewById<MaterialToolbar>(R.id.top_app_bar)
        setToolbar(toolBar)
        toolBar?.title = "Parent Fragment"
    }

    private fun setToolbar( toolBar: MaterialToolbar){
        this.toolBar = toolBar
    }
    private fun initViewModel(){
        var infiniteViewModelFactory = InfiniteViewModelFactory()
        infiniteListViewModel = ViewModelProvider(this,infiniteViewModelFactory).get(
            InfiniteListViewModel::class.java)

    }

    internal  fun fetchToolBar() = toolBar


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ParentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ParentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}