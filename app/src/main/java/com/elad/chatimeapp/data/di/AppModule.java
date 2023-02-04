package com.elad.chatimeapp.data.di;

import android.content.Context;

import com.elad.chatimeapp.data.firebase.DatabaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

/**
 * @author - Elad Sabag
 * @date - 1/21/2023
 */
@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    @Singleton
    public DatabaseRepository provideDatabaseRepository(FirebaseDatabase mDatabase) {
        return new DatabaseRepository(mDatabase);
    }

    @Provides
    @Singleton
    public Context provideAppContext(@ApplicationContext Context context) {
        return context.getApplicationContext();
    }
}
