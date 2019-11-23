package com.example.quiz;

import android.provider.BaseColumns;

/**
 * Az SQLite műveletekhez kell.
 */

final class QuizContract {
    /**
     * Konstruktor
     */
    private QuizContract() {
    }

    /**
     * A konstans változók könnyebb eléréséhez
     * A BaseColums-ra az _ID, miatt van szükség, mert automatikusan ikrementálódik
     */
    public static class QuestionsTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NR = "answer_nr";
    }
}
