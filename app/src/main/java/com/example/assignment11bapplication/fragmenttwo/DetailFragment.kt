package com.example.assignment11bapplication.fragmenttwo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.assignment11bapplication.R
import com.example.assignment11bapplication.databinding.FragmentDetailBinding
import com.example.assignment11bapplication.databinding.FragmentListBinding
import com.example.assignment11bapplication.model.Article


class DetailFragment : Fragment() {
    private val args:DetailFragmentArgs by navArgs()
    private lateinit var binding:FragmentDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false)



        // Set the article details to the views
        binding.article=args.result

        return binding.root
    }

    companion object {
        private const val ARG_ARTICLE = "article"

        // Function to create a new instance of DetailFragment
        fun newInstance(article: Article): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle().apply {
                putParcelable(ARG_ARTICLE, article) // Ensure Article implements Parcelable
            }
            fragment.arguments = args
            return fragment
        }
    }

}