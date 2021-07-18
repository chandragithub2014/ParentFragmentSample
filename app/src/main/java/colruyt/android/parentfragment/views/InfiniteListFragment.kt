package colruyt.android.parentfragment.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import colruyt.android.parentfragment.R
import com.google.android.material.appbar.MaterialToolbar
import colruyt.android.parentfragment.adapter.InfiniteAdapter
import colruyt.android.parentfragment.interfaces.LoadMore
import colruyt.android.parentfragment.model.InfiniteAdapterModel
import colruyt.android.parentfragment.model.InfiniteModel
import colruyt.android.parentfragment.responsehelper.ResultOf
import colruyt.android.parentfragment.viewmodel.InfiniteListViewModel
import kotlinx.android.synthetic.main.fragment_infinite_list.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfiniteListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfiniteListFragment : Fragment() , LoadMore {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var infiniteListView : View
    private var mContainer : Int = -1
    lateinit var infiniteListViewModel: InfiniteListViewModel
   // private val infiniteListViewModel: InfiniteListViewModel by viewModels({ requireParentFragment() })
    private var infiniteModel: InfiniteModel? = null
    private var position:Int = 0
    var infiniteAdapter: InfiniteAdapter? = null
    private var startPosition:Number = 0
    private var endPosition:Number = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@InfiniteListFragment).navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        infiniteListView =  inflater.inflate(R.layout.fragment_infinite_list, container, false)

        return infiniteListView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
   //     handleScanLayout(isEnabled = true)
        initAdapter()
        infiniteListViewModel.obtainInfiniteList()
        observeViewModel()
    }

    private fun initViewModel(){
       // val navHostFragment = parentFragment as NavHostFragment?


     //   infiniteListViewModel =    (parentFragment as ParentFragment).fetchInfiniteViewModel()
        infiniteListViewModel =  ViewModelProvider(requireParentFragment().requireParentFragment()).get<InfiniteListViewModel>()

    }

    private fun observeViewModel(){
        infiniteListViewModel.obtainInfiniteListResponse.observe(viewLifecycleOwner, Observer {
            it?.let {
                when(it){
                    is ResultOf.Success -> {
                        infiniteModel = it.value
                       infiniteModel?.let { model ->
                           if(model.size >=10){
                               startPosition = 0
                               endPosition = 10
                               val preparedData = infiniteListViewModel.prepareDataForAdapter(model,startPosition.toInt(),
                                   endPosition.toInt())

                               populateAdapter(preparedData,startPosition,endPosition,model)
                           }

                       }

                    }

                    is ResultOf.Failure -> {
                        val failedMessage =  it.message ?: "Unknown Error"
                        println("Failed Message $failedMessage")
                    }
                }
            }
        })
    }

    private fun populateAdapter(infiniteAdapterModelList : MutableList<InfiniteAdapterModel>, startPosition:Number, endPosition:Number, originalModel: InfiniteModel){

        infiniteAdapter?.let {
            val layoutManager = LinearLayoutManager(context)
              infinite_rv.layoutManager = layoutManager
             infinite_rv.adapter = it
            infinite_rv.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            it.setFiniteList(startPosition,endPosition,infiniteAdapterModelList,originalModel,this)
            it.notifyDataSetChanged()
            progressBar.visibility = View.GONE
        }
    }
    private fun initAdapter(){
        infiniteAdapter = InfiniteAdapter(this@InfiniteListFragment.requireActivity(), mutableListOf())
        infiniteAdapter?.let {
            val layoutManager = LinearLayoutManager(context)
            infinite_rv.layoutManager = layoutManager
            infinite_rv.adapter = it
            infinite_rv.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            it.notifyDataSetChanged()
        }
    }
    private fun initToolBar(){
        val navHostFragment = parentFragment as NavHostFragment?
        navHostFragment?.let { navHostFragment ->
            navHostFragment.parentFragment?.let { parentFrag ->
                var toolbar =   parentFrag.view?.findViewById<MaterialToolbar>(R.id.top_app_bar)
                toolbar?.title = "Infinite List Fragment"
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfiniteListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfiniteListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun loadMore(
        startPosition: Int,
        endPosition: Int,
        previousList: MutableList<InfiniteAdapterModel>,
        originalModel: InfiniteModel
    ) {
        previousList.removeAt(previousList.size -1)
        val scrollPosition = previousList.size
        infiniteAdapter?.let {
            it.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            var nextLimit = currentSize+10
            if(nextLimit<=originalModel.size) {
                val preparedData = infiniteListViewModel.prepareDataForAdapter(
                    originalModel, currentSize,
                    nextLimit
                          //  it.setFiniteList(startPosition,endPosition,infiniteAdapterModelList,originalModel,this)
                )
                previousList.addAll(preparedData)
                it.setFiniteList(currentSize,nextLimit,previousList,originalModel,this)
                it.notifyDataSetChanged()
            }else if(currentSize<originalModel.size){
                nextLimit =  originalModel.size
                val preparedData = infiniteListViewModel.prepareDataForAdapter(
                    originalModel, currentSize,
                    nextLimit
                    //  it.setFiniteList(startPosition,endPosition,infiniteAdapterModelList,originalModel,this)
                )
                previousList.addAll(preparedData)
                currentSize = originalModel.size
                it.setFiniteList(currentSize,nextLimit,previousList,originalModel,this)
                it.notifyDataSetChanged()
            }

        }
    }


}

//https://stackoverflow.com/questions/60003039/why-android-navigation-component-screen-not-go-back-to-previous-fragment-but-a-m
//https://stackoverflow.com/questions/60475611/get-parent-fragment-from-child-when-using-navigation-component