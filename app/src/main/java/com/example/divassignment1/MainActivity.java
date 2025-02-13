package com.example.divassignment1;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.divassignment1.databinding.ActivityMainBinding;

/**
 * MainActivity class for a simple Wear OS stopwatch app.
 * Implements ViewBinding and OnClickListener for handling button clicks.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding; // ViewBinding instance for accessing UI elements
    private Handler timeHandler; // Handler for updating the stopwatch display
    private long startTimestamp, elapsedDuration; // Track elapsed time
    private boolean isActive; // Boolean to track if the stopwatch is running

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Initialize ViewBinding
        setContentView(binding.getRoot());

        // Initialize the handler for updating the stopwatch display
        timeHandler = new Handler();

        // Set click listeners using implemented interface
        binding.btnStart.setOnClickListener(this);
        binding.btnStop.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
    }

    /**
     * Handles button clicks based on their IDs.
     */
    @Override
    public void onClick(View v) {
        if (v == binding.btnStart) {
            startTimer();
        } else if (v == binding.btnStop) {
            pauseTimer();
        } else if (v == binding.btnReset) {
            resetTimer();
        }
    }

    /**
     * Starts the stopwatch by recording the current time and scheduling updates.
     */
    private void startTimer() {
        startTimestamp = SystemClock.elapsedRealtime() - elapsedDuration; // Adjust start time
        isActive = true;
        binding.btnStart.setEnabled(false);
        binding.btnStop.setEnabled(true);
        binding.btnReset.setEnabled(false);
        timeHandler.postDelayed(updateTimerRunnable, 0); // Start the update loop
    }

    /**
     * Pauses the stopwatch and stops updating the display.
     */
    private void pauseTimer() {
        isActive = false;
        timeHandler.removeCallbacks(updateTimerRunnable); // Stop updating the UI
        binding.btnStart.setEnabled(true);
        binding.btnStop.setEnabled(false);
        binding.btnReset.setEnabled(true);
    }

    /**
     * Resets the stopwatch to zero and updates the UI.
     */
    private void resetTimer() {
        elapsedDuration = 0;
        binding.stopwatchDisplay.setText(getString(R.string.time_default)); // Reset display
        binding.btnStart.setEnabled(true);
        binding.btnStop.setEnabled(false);
        binding.btnReset.setEnabled(false);
    }

    /**
     * Runnable that updates the stopwatch display every 10ms.
     */
    private final Runnable updateTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isActive) {
                elapsedDuration = SystemClock.elapsedRealtime() - startTimestamp; // Calculate elapsed time
                long minutes = (elapsedDuration / 1000) / 60;
                long seconds = (elapsedDuration / 1000) % 60;
                long milliseconds = (elapsedDuration % 1000);

                // Update UI with formatted time string
                binding.stopwatchDisplay.setText(
                        String.format(getString(R.string.time_format), minutes, seconds, milliseconds));

                // Schedule next update in 10ms
                timeHandler.postDelayed(this, 10);
            }
        }
    };
}
