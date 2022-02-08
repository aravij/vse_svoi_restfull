package com.vsesvoi.entity

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("execute_locally")
@DisplayName("User tests")
internal class UserTest {

    @Nested
    @Tag("entity_methods")
    @DisplayName("Test pure entities methods")
    inner class EntityFunctionality {
        @Test
        fun `add community to user`() {
            val (user, community) = SyntheticEntities()

            user.add(community)

            user.communities shouldHaveSingleElement community
            community.users shouldHaveSingleElement user
        }

        @Test
        fun `remove community from user`() {
            val (user, community) = SyntheticEntities()

            user.add(community)
            user.remove(community)

            user.communities.shouldBeEmpty()
            community.users.shouldBeEmpty()
        }

        @Test
        fun `add wish to user`() {
            val (user, _, wish) = SyntheticEntities()

            user.add(wish)

            user.wishes shouldHaveSingleElement wish
            wish.users shouldHaveSingleElement user
        }

        @Test
        fun `remove wish from user`() {
            val (user, _, wish) = SyntheticEntities()

            user.add(wish)
            user.remove(wish)

            user.wishes.shouldBeEmpty()
            wish.users.shouldBeEmpty()
        }
    }
}