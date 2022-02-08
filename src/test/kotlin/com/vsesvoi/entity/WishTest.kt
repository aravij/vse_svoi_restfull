package com.vsesvoi.entity

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test


@Tag("execute_locally")
@DisplayName("Wish tests")
internal class WishTest {

    @Nested
    @Tag("entity_methods")
    @DisplayName("Test pure entities methods")
    inner class EntityFunctionality {

        @Test
        fun `add wish to community in constructor`() {
            // the wiring was done during creation of wish in SyntheticEntities
            val (_, community, wish) = SyntheticEntities()

            wish.community shouldBe community
            community.wishes shouldHaveSingleElement wish
        }

        @Test
        fun `add user to wish`() {
            val (user, _, wish) = SyntheticEntities()

            wish.add(user)

            wish.users shouldHaveSingleElement user
            user.wishes shouldHaveSingleElement wish
        }

        @Test
        fun `remove user from wish`() {
            val (user, _, wish) = SyntheticEntities()

            wish.add(user)
            wish.remove(user)

            wish.users.shouldBeEmpty()
            user.wishes.shouldBeEmpty()
        }
    }
}