package com.example.assignment11bapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.assignment11application.retrofit.RetrofitInstance
import com.example.assignment11bapplication.R
import com.example.assignment11bapplication.database.NewsDatabase
import com.example.assignment11bapplication.databinding.FragmentListBinding
import com.example.assignment11bapplication.model.Article
import com.example.assignment11bapplication.repository.NewsRepository


class ListFragment : Fragment() , OnItemClickListener{
    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ArticleListAdapter
    private lateinit var viewmodel: ListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_list,container,false)
        adapter= ArticleListAdapter(this)
        binding.photosGrid.adapter=adapter
        binding.photosGrid.layoutManager=StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        val application = requireNotNull(this.activity).application
        val database = NewsDatabase.getDatabase(application) // Get the instance of the database
        val repository = NewsRepository(database, RetrofitInstance.NewsApi) // Create repository
        val viewModelFactory = NewsViewModelFactory(repository) // Create ViewModelFactory

        viewmodel = ViewModelProvider(this,viewModelFactory).get(ListViewModel::class.java)
        binding.viewModel = viewmodel
        binding.lifecycleOwner = this

        viewmodel.properties.observe(viewLifecycleOwner) { articles ->
            adapter.submitList(articles)
        }

        setHasOptionsMenu(true)
        return binding.root


    }

    // Handle click events
    override fun onClick(article: Article) {
        // Do something when the item is clicked, for example:
        Toast.makeText(context, "Clicked on: ${article.title}", Toast.LENGTH_SHORT).show()
        val action = ListFragmentDirections.actionListFragmentToDetailFragment(article)
        Log.d("DetailFragment", "Received Article: $article")
        findNavController().navigate(action)
    }

}