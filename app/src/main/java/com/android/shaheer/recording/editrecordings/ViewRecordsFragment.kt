package com.android.shaheer.recording.editrecordings


import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife

import com.android.shaheer.recording.R
import com.android.shaheer.recording.utils.SessionManager

class ViewRecordsFragment : Fragment() {

    @BindView(R.id.rv_recordings) lateinit var rvRecordings: RecyclerView
    @BindView(R.id.toolbar) lateinit var toolbar: Toolbar


    private lateinit var viewModel: ViewRecordsViewModel

    private val recordingAdapter = RecordingListAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        viewModel = ViewModelProvider(
                viewModelStore,
                ViewRecordsViewModelFactory(SessionManager(context.applicationContext))
        ).get(ViewRecordsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_view_records, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            toolbar.navigationIcon = context?.getDrawable(R.drawable.ic_arrow_back)
        }else{
            toolbar.navigationIcon = context?.resources?.getDrawable(R.drawable.ic_arrow_back)
        }
        toolbar.title = getString(R.string.recording)

        rvRecordings.adapter = recordingAdapter

        viewModel.recordings.observe(viewLifecycleOwner, Observer { recordingAdapter.submitList(it) })
    }

    override fun onResume() {
        super.onResume()
        context?.let { viewModel.getRecordingsFromFiles(it) }
    }

}