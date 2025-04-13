package com.example.moviedb.database

import  com.example.moviedb.models.Movie

class Movies {
    fun getMovies(): List<Movie>{
        return listOf(
            Movie(
                278,
                "tt0111161",
                "The Shawshank Redemption",
                "/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg",
                "/zfbjgQE1uSd9wiPTX4VzsLi0rGG.jpg",
                "1994-09-23",
                "Imprisoned in the 1940s for the double murder of his wife and her lover, upstanding banker Andy Dufresne begins a new life at the Shawshank prison, where he puts his accounting skills to work for an amoral warden. During his long stretch in prison, Dufresne comes to be admired by the other inmates -- including an older prisoner named Red -- for his integrity and unquenchable sense of hope."
            ),
            Movie(
                238,
                "tt0068646",
                "The Godfather",
                "/3bhkrj58Vtu7enYsRolD1fZdja1.jpg",
                "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg",
                "1972-03-14",
                "Spanning the years 1945 to 1955, a chronicle of the fictional Italian-American Corleone crime family. When organized crime family patriarch, Vito Corleone barely survives an attempt on his life, his youngest son, Michael steps in to take care of the would-be killers, launching a campaign of bloody revenge."
            ),
            Movie(
                424,
                "tt0108052",
                "Schindler's List",
                "/sF1U4EUQS8YHUYjNl3pMGNIQyr0.jpg",
                "/zb6fM1CX41D9rF9hdgclu0peUmy.jpg",
                "1993-12-15",
                "The true story of how businessman Oskar Schindler saved over a thousand Jewish lives from the Nazis while they worked as slaves in his factory during World War II."
            ),
            Movie(
                129,
                "tt0245429",
                "千と千尋の神隠し",
                "/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg",
                "/6oaL4DP75yABrd5EbC4H2zq5ghc.jpg",
                "2001-07-20",
                "A young girl, Chihiro, becomes trapped in a strange new world of spirits. When her parents undergo a mysterious transformation, she must call upon the courage she never knew she had to free her family."
            ),
            Movie(
                389,
                "tt0050083",
                "12 Angry Men",
                "/ow3wq89wM8qd5X7hWKxiRfsFf9C.jpg",
                "/bxgTSUenZDHNFerQ1whRKplrMKF.jpg",
                "1957-04-10",
                "The defense and the prosecution have rested and the jury is filing into the jury room to decide if a young Spanish-American is guilty or innocent of murdering his father. What begins as an open and shut case soon becomes a mini-drama of each of the jurors' prejudices and preconceptions about the trial, the accused, and each other."
            )
        )

    }
}