package com.vsesvoi.entity

internal data class SyntheticEntities(
    val user: User = User("user name"),
    val community: Community = Community("community name"),
    val wish: Wish = Wish("wish title", community)
)
