package com.vsesvoi.entity

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSingleElement
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

    @Nested
    @DataJpaTest
    @SpringJUnitConfig
    @Tag("mapping_functionality")
    @DisplayName("Test mapping functionality")
    @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
    inner class MappingFunctionality(val entityManager: TestEntityManager) {

        @Test
        fun `persist and find`() {
            val (_, _, wish) = SyntheticEntities()

            entityManager.persistFlushFind(wish) shouldBe wish
        }

        @Test
        fun `persist community on cascade`() {
            // the wiring was done during creation of wish in SyntheticEntities
            val (_, community, wish) = SyntheticEntities()

            entityManager.persistAndFlush(wish)

            entityManager.find(community.javaClass, wish.community.id) shouldBe community
        }

        @Test
        fun `do not delete community on cascade`() {
            // the wiring was done during creation of wish in SyntheticEntities
            val (_, community, wish) = SyntheticEntities()

            entityManager.persistAndFlush(wish)

            // check community got persisted
            entityManager.find(community.javaClass, community.id).shouldNotBeNull()

            entityManager.remove(wish)

            // check that community wasn't removed
            entityManager.find(community.javaClass, community.id).shouldNotBeNull()
        }

        @Test
        fun `persist wish on cascade`() {
            val (user, _, wish) = SyntheticEntities()

            wish.add(user)

            entityManager.persistAndFlush(wish)

            entityManager.find(user.javaClass, wish.users.first().id) shouldBe user
        }

        @Test
        fun `do not delete user on cascade`() {
            val (user, _, wish) = SyntheticEntities()

            wish.add(user)

            entityManager.persistAndFlush(wish)

            // check user got persisted
            entityManager.find(user.javaClass, user.id).shouldNotBeNull()

            entityManager.remove(wish)

            // check that user wasn't removed
            entityManager.find(user.javaClass, user.id).shouldNotBeNull()
        }
    }
}
