package com.example.recipeapp.dao

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.recipeapp.models.FavouriteMeal

private const val AUTHORITY = "com.example.recipeapp"

private const val PATH_FAVOURITES = "favourites"

val FAVOURITES_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH_FAVOURITES")

private const val FAVOURITES = 1
private const val FAVOURITE_ID = 2


private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH_FAVOURITES, FAVOURITES)
    addURI(AUTHORITY, "$PATH_FAVOURITES/#", FAVOURITE_ID)
    this
}

class MealContentProvider : ContentProvider() {
    private lateinit var database: MealsSqlHelper;

    override fun onCreate(): Boolean {
        database = getDatabase(context)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = database.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = database.insert(values)
        return ContentUris.withAppendedId(FAVOURITES_URI, id)
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        when(URI_MATCHER.match(uri)) {
            FAVOURITES -> return database.delete(selection, selectionArgs)
            FAVOURITE_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return database.delete("${FavouriteMeal::mealId.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("WRONG URI")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)) {
            FAVOURITES -> return database.update(values, selection, selectionArgs)
            FAVOURITE_ID -> {
                val id = uri.lastPathSegment
                if (id != null) {
                    return database.update(values, "${FavouriteMeal::mealId.name}=?", arrayOf(id))
                }
            }
        }
        throw IllegalArgumentException("WRONG URI")
    }
}