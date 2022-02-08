package com.vsesvoi.entity

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

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

    @Nested
    @DataJpaTest
    @SpringJUnitConfig
    @Tag("mapping_functionality")
    @DisplayName("Test mapping functionality")
    @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
    inner class MappingFunctionality(val entityManager: TestEntityManager) {
        @Test
        fun `persist and find`() {
            val (_, community) = SyntheticEntities()

            entityManager.persistFlushFind(community) shouldBe community
        }

        @Test
        fun `persist users on cascade`() {
            val (user, community) = SyntheticEntities()
            community.add(user)

            entityManager.persistAndFlush(community)

            entityManager.find(user.javaClass, community.users.first().id) shouldBe user
        }

        @Test
        fun `do not delete users on cascade`() {
            val (user, community) = SyntheticEntities()

            community.add(user)
            entityManager.persistAndFlush(community)

            // check that user got persisted
            entityManager.find(user.javaClass, user.id).shouldNotBeNull()

            entityManager.remove(community)

            // check that user wasn't removed
            entityManager.find(user.javaClass, user.id).shouldNotBeNull()
        }

        @Test
        fun `persist wishes on cascade`() {
            val (_, community, wish) = SyntheticEntities()

            // addition if wish to community was done during creation in SyntheticEntities

            entityManager.persistAndFlush(community)

            entityManager.find(wish.javaClass, community.wishes.first().id) shouldBe wish
        }

        @Test
        fun `delete wishes on cascade`() {
            val (_, community, wish) = SyntheticEntities()

            entityManager.persistAndFlush(community)

            // check that wish got persisted
            entityManager.find(wish.javaClass, wish.id).shouldNotBeNull()

            entityManager.remove(community)

            // check that wish got removed too
            entityManager.find(wish.javaClass, wish.id).shouldBeNull()
        }
    }
}
