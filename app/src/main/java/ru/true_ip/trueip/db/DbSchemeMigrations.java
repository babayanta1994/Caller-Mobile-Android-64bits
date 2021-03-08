package ru.true_ip.trueip.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.support.annotation.NonNull;

/**
 * Created by ektitarev on 03.09.2018.
 *
 */

public class DbSchemeMigrations {

    //private static final String TAG = DbSchemeMigrations.class.getSimpleName();

    public static final Migration[] MIGRATIONS = new Migration[] {
            MigrationFrom1to2(),
            MigrationFrom2to3(),
            MigrationFrom1to3(),
            MigrationFrom1to4(),
            MigrationFrom2to4(),
            MigrationFrom3to4(),
            MigrationFrom1to5(),
            MigrationFrom2to5(),
            MigrationFrom3to5(),
            MigrationFrom4to5(),
            MigrationFrom1to6(),
            MigrationFrom2to6(),
            MigrationFrom3to6(),
            MigrationFrom4to6(),
            MigrationFrom5to6(),
    };

    private static Migration MigrationFrom1to2() {
        return new Migration(1, 2) {

                    @Override
                    public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                        supportSQLiteDatabase.compileStatement(
                                "ALTER TABLE Devices ADD is_callable INTEGER"
                        ).execute();
                    }
                };
    }

    private static Migration MigrationFrom2to3() {
        return new Migration(2, 3) {

            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                // remove old objects due to new architecture
                supportSQLiteDatabase.compileStatement(
                        "DELETE FROM Objects WHERE is_cloud = 1"
                ).execute();

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD server_url TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_blocked INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_server_active INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD license_type TEXT"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom1to3() {
        return new Migration(1, 3) {

            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {

                MigrationFrom1to2().migrate(supportSQLiteDatabase);

                // remove old objects due to new architecture
                supportSQLiteDatabase.compileStatement(
                        "DELETE FROM Objects WHERE is_cloud = 1"
                ).execute();

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD server_url TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_blocked INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_server_active INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD license_type TEXT"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom1to4() {
        return new Migration(1, 4) {

            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom1to2().migrate(supportSQLiteDatabase);

                // remove old objects due to new architecture
                supportSQLiteDatabase.compileStatement(
                        "DELETE FROM Objects WHERE is_cloud = 1"
                ).execute();

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD server_url TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_blocked INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_server_active INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD license_type TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_object_active INTEGER DEFAULT 1"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom2to4() {
        return new Migration(2, 4) {

            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                // remove old objects due to new architecture
                supportSQLiteDatabase.compileStatement(
                        "DELETE FROM Objects WHERE is_cloud = 1"
                ).execute();

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD server_url TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_blocked INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_server_active INTEGER"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD license_type TEXT"
                ).execute();
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_object_active INTEGER DEFAULT 1"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom3to4() {
        return new Migration(3, 4) {

            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Objects ADD is_object_active INTEGER DEFAULT 1"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom1to5() {
        return new Migration(1, 5) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom1to4().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement("CREATE TABLE Messages ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "review_id INTEGER, " +
                        "user_id INTEGER, " +
                        "is_concierge INTEGER, " +
                        "is_viewed INTEGER, " +
                        "apartment TEXT, " +
                        "msg_text TEXT, " +
                        "created_at TEXT, " +
                        "updated_at TEXT, " +
                        "answers_count INTEGER" +
                        ")").execute();
            }
        };
    }

    private static Migration MigrationFrom2to5() {
        return new Migration(2, 5) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom2to4().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement("CREATE TABLE Messages ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "review_id INTEGER, " +
                        "user_id INTEGER, " +
                        "is_concierge INTEGER, " +
                        "is_viewed INTEGER, " +
                        "apartment TEXT, " +
                        "msg_text TEXT, " +
                        "created_at TEXT, " +
                        "updated_at TEXT, " +
                        "answers_count INTEGER" +
                        ")").execute();
            }
        };
    }

    private static Migration MigrationFrom3to5() {
        return new Migration(3, 5) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom3to4().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement("CREATE TABLE Messages ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "review_id INTEGER, " +
                        "user_id INTEGER, " +
                        "is_concierge INTEGER, " +
                        "is_viewed INTEGER, " +
                        "apartment TEXT, " +
                        "msg_text TEXT, " +
                        "created_at TEXT, " +
                        "updated_at TEXT, " +
                        "answers_count INTEGER" +
                        ")").execute();
            }
        };
    }

    private static Migration MigrationFrom4to5() {
        return new Migration(4, 5) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.compileStatement("CREATE TABLE Messages ( " +
                        "id INTEGER PRIMARY KEY, " +
                        "review_id INTEGER, " +
                        "user_id INTEGER, " +
                        "is_concierge INTEGER, " +
                        "is_viewed INTEGER, " +
                        "apartment TEXT, " +
                        "msg_text TEXT, " +
                        "created_at TEXT, " +
                        "updated_at TEXT, " +
                        "answers_count INTEGER" +
                        ")").execute();
            }
        };
    }

    private static Migration MigrationFrom1to6() {
        return new Migration(1, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom1to5().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Devices ADD device_server_id INTEGER"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom2to6() {
        return new Migration(2, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom2to5().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Devices ADD device_server_id INTEGER"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom3to6() {
        return new Migration(3, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom3to5().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Devices ADD device_server_id INTEGER"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom4to6() {
        return new Migration(4, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                MigrationFrom4to5().migrate(supportSQLiteDatabase);

                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Devices ADD device_server_id INTEGER"
                ).execute();
            }
        };
    }

    private static Migration MigrationFrom5to6() {
        return new Migration(5, 6) {
            @Override
            public void migrate(@NonNull SupportSQLiteDatabase supportSQLiteDatabase) {
                supportSQLiteDatabase.compileStatement(
                        "ALTER TABLE Devices ADD device_server_id INTEGER"
                ).execute();
            }
        };
    }
}
