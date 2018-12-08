<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:mContext=".fragments.NewExerciseDialogFragment">

    <Button
        android:id="@+id/button_exercise_confirm"
        android:layout_width="150dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="8dp"
        android:layout_marginLeft="8dp"
        android:layout_marginEnd="24dp"
        android:layout_marginRight="24dp"
        android:text="@string/confirm"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toEndOf="@+id/button_exercise_cancel"
        app:layout_constraintTop_toTopOf="@+id/button_exercise_cancel" />

    <Button
        android:id="@+id/button_exercise_cancel"
        android:layout_width="150dp"
        android:layout_height="wrap_content"
        android:layout_marginStart="24dp"
        android:layout_marginLeft="24dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="8dp"
        android:layout_marginRight="8dp"
        android:text="@android:string/cancel"
        app:layout_constraintEnd_toStartOf="@+id/button_exercise_confirm"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/text_exercise_units" />

    <Spinner
        android:id="@+id/sp_exercise"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginLeft="16dp"
        app:layout_constraintBottom_toBottomOf="@+id/text_exercise_name"
        app:layout_constraintStart_toEndOf="@+id/text_exercise_name"
        app:layout_constraintTop_toTopOf="@+id/text_exercise_name" />

    <TextView
        android:id="@+id/text_exercise_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginLeft="16dp"
        andr