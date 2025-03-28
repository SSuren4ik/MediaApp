package com.mediaapp.user_search.presentation.search_screen

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mediaapp.core.utils.SearchDelayedTextWatcher
import com.mediaapp.core.utils.SelectedUserPlaylistsLauncher
import com.mediaapp.user_search.databinding.ActivityUserSearchBinding
import com.mediaapp.user_search.di.UserSearchDepsProvider
import com.mediaapp.user_search.presentation.search_screen.viewmodel.UserSearchViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSearchBinding
    private val viewModel: UserSearchViewModel by viewModels()
    private val adapter: UserSearchRecyclerViewAdapter by lazy {
        UserSearchRecyclerViewAdapter(selectedUserPlaylistsLauncher)
    }

    @Inject
    lateinit var selectedUserPlaylistsLauncher: SelectedUserPlaylistsLauncher
    private val diffCallback = UserSearchDiffCallback()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        initDI()
        initUI()
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.response.collect { users ->
                when {
                    users.isNotEmpty() -> {
                        adapter.setData(users, diffCallback)
                    }

                    else -> showToast("No users found")
                }
            }
        }
    }

    private fun initUI() {
        binding.searchEditText.apply {
            addTextChangedListener(SearchDelayedTextWatcher(coroutineScope = lifecycleScope) { prefix ->
                viewModel.findUserByPrefix(prefix)
            })
            isSingleLine = true
        }
        setInsets()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@UserSearchActivity, RecyclerView.VERTICAL, false)
            adapter = this@UserSearchActivity.adapter
            val spacing =
                resources.getDimensionPixelSize(com.mediaapp.design_system.R.dimen.item_spacing)
            addItemDecoration(UserSearchItemDecoration(spacing))
        }
    }

    private fun initDI() {
        val component = (application as UserSearchDepsProvider).getUserSearchComponent()
        component.inject(viewModel)
        component.inject(this)
    }

    private fun setInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.searchLinear) { view, insets ->
            val innerPadding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )
            view.setPadding(0, innerPadding.top, 0, 0)
            insets
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}