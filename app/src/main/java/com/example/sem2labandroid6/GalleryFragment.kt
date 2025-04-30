package com.example.sem2labandroid6

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sem2labandroid6.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {
    private val viewModel: ImagesViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var binding: FragmentGalleryBinding
    private val permissionRequestCode = 100


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPermissions()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = ImagesAdapter(
                onLongClick = { position ->
                    showDescriptionEditor(position)
                },
                onClick = { item ->
                    navigateToDetail(item)
                }
            )
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        viewModel.images.observe(viewLifecycleOwner) { items ->
            (binding.rView.adapter as? ImagesAdapter)?.apply {
                updateItems(items)
                binding.rView.post { binding.rView.requestLayout() }
            }
        }
    }

    private fun setupPermissions() {
        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                Manifest.permission.READ_MEDIA_IMAGES

            else ->
                Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.loadImages()
        } else {
            requestPermissions(arrayOf(permission), permissionRequestCode)
        }
    }

    @Deprecated("Deprecated in Fragment")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadImages()
        } else {
        }
    }

    private fun showDescriptionEditor(position: Int) {
        val currentItem = viewModel.images.value?.get(position) ?: return

        val direction = GalleryFragmentDirections.actionToEdit(
            mediaId = currentItem.mediaId,
            description = currentItem.description
        )
        findNavController().navigate(direction)

        setupResultListener()
    }

    private fun navigateToDetail(item: ImageItem) {
        val direction = GalleryFragmentDirections.actionGalleryToDetail(
            mediaUri = item.uri.toString(),
            description = item.description
        )
        findNavController().navigate(direction)
    }

    private fun setupResultListener() {
        parentFragmentManager.setFragmentResultListener(
            EditDescriptionFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { _, result ->
            val mediaId = result.getLong("mediaId")
            val newDescription = result.getString(EditDescriptionFragment.DESCRIPTION_KEY) ?: ""
            val oneWitnMinus: Long = -1
            if (mediaId != oneWitnMinus) {
                viewModel.updateDescription(mediaId, newDescription)
            }
        }
    }
}