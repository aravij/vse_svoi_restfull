package com.vsesvoi.entity

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("execute_locally")
@DisplayName("Community tests")
internal class CommunityTest {

    @Nested
    @Tag("entity_methods")
    @DisplayName("Test pure entities methods")
    inner class EntityFunctionality {
        @Test
        fun `add user to community`() {
            val (user, community) = SyntheticEntities()

            community.add(user)

            community.users shouldHaveSingleElement user
            user.communities shouldHaveSingleElement community
        }

        @Test
        fun `remove user from community`() {
            val (user, community) = SyntheticEntities()

            community.add(user)
            community.remove(user)

            community.users.shouldBeEmpty()
            user.communities.shouldBeEmpty()
        }
    }
}
