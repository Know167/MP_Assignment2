package com.example.assignment2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProgressDialogFragment : DialogFragment() {

    private lateinit var progressTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_progress_dialog, container, false)
        Toast.makeText(view.context,"progress dialogue fragment created", Toast.LENGTH_SHORT).show()

        progressTextView = view.findViewById(R.id.textView_progress)

        displayProgress()

        return view
    }

    private fun displayProgress() {
        val sharedPreferences = requireActivity().getSharedPreferences("workout_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val workoutsJson = sharedPreferences.getString("workouts", "[]")
        val typeToken = object : TypeToken<MutableList<LogWorkoutFragment.Workout>>() {}.type
        val workouts: MutableList<LogWorkoutFragment.Workout> = gson.fromJson(workoutsJson, typeToken)

        if (workouts.isEmpty()) {
            progressTextView.text = "No workout data available."
        } else {
            val progressSummary = workouts.joinToString(separator = "\n\n") { workout ->
                "Duration: ${workout.duration} minutes\nType: ${workout.type}\nIntensity: ${workout.intensity}"
            }
            progressTextView.text = progressSummary
        }
    }
}
