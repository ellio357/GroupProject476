package edu.msu.ellio357.groupproject476;

import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class WorkoutDatabase {

    // Workout model class
    public static class Workout {
        private String id;
        private String name;
        private int length;
        private int calories;
        private String userId;
        private Timestamp createdAt;

        // Empty constructor needed for Firestore
        public Workout() {
        }

        public Workout(String name, int length, int calories, String userId) {
            this.name = name;
            this.length = length;
            this.calories = calories;
            this.userId = userId;
            this.createdAt = Timestamp.now();
        }

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getCalories() {
            return calories;
        }

        public void setCalories(int calories) {
            this.calories = calories;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }
    }

    // Repository functionality
    private final CollectionReference workoutsCollection;

    public WorkoutDatabase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        workoutsCollection = db.collection("workouts");
    }

    // Add a new workout
    public Task<DocumentReference> addWorkout(String name, int length, int calories) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Workout workout = new Workout(name, length, calories, userId);
        return workoutsCollection.add(workout);
    }

    // Get all workouts for current user
    public Task<QuerySnapshot> getWorkoutsForCurrentUser() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        return workoutsCollection
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get();
    }

    // Delete a workout
    public Task<Void> deleteWorkout(String workoutId) {
        return workoutsCollection.document(workoutId).delete();
    }

    // Update a workout
    public Task<Void> updateWorkout(String workoutId, String name, int length, int calories) {
        DocumentReference workoutRef = workoutsCollection.document(workoutId);

        return workoutRef.update(
                "name", name,
                "length", length,
                "calories", calories
        );
    }
}