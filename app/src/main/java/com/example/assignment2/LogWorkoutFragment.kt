package com.example.assignment2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class LogWorkoutFragment : Fragment() {
    data class Workout(val duration: String, val type: String, val intensity: String)

    private lateinit var workoutDurationEditText: EditText
    private lateinit var workoutTypeSpinner: Spinner
    private lateinit var workoutIntensityRadioGroup: RadioGroup
    private lateinit var saveButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_log_workout, container, false)
Toast.makeText(view.context,"log workout fragment created",Toast.LENGTH_SHORT).show()
        workoutDurationEditText = view.findViewById(R.id.editText_workout_duration)
        workoutTypeSpinner = view.findViewById(R.id.spinner_workout_type)
        workoutIntensityRadioGroup = view.findViewById(R.id.radioGroup_workout_intensity)
        saveButton = view.findViewById(R.id.button_save)
        val workoutTypes = arrayOf("Running", "Weightlifting", "Yoga", "Cycling")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, workoutTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        workoutTypeSpinner.adapter = adapter

        saveButton.setOnClickListener {
            saveWorkout()
        }

        return view
    }

    private fun saveWorkout() {
        val duration = workoutDurationEditText.text.toString()
        val type = workoutTypeSpinner.selectedItem.toString()
        val intensity = when (workoutIntensityRadioGroup.checkedRadioButtonId) {
            R.id.radio_low -> "Low"
            R.id.radio_medium -> "Medium"
            R.id.radio_high -> "High"
            else -> ""
        }

        if (duration.isEmpty() || intensity.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = requireActivity().getSharedPreferences("workout_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val workoutsJson = sharedPreferences.getString("workouts", "[]")
        val typeToken = object : TypeToken<MutableList<Workout>>() {}.type
        val workouts: MutableList<Workout> = gson.fromJson(workoutsJson, typeToken)

        val newWorkout = Workout(duration, type, intensity)
        workouts.add(newWorkout)

        val updatedWorkoutsJson = gson.toJson(workouts)
        val editor = sharedPreferences.edit()
        editor.putString("workouts", updatedWorkoutsJson)
        editor.apply()
        workoutDurationEditText.text.clear()
        workoutTypeSpinner.setSelection(0)
        workoutIntensityRadioGroup.clearCheck()
        Toast.makeText(requireContext(), "Workout saved", Toast.LENGTH_SHORT).show()
    }
}
