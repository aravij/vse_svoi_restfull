package com.vsesvoi.entity

import com.vsesvoi.misc.TagNames
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

@Tag(TagNames.EXECUTE_LOCALLY)
@DisplayName("User tests")
internal class UserTest {

    @Nested
    @Tag(TagNames.ENTITY_METHODS)
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

    @Nested
    @DataJpaTest
    @SpringJUnitConfig
    @Tag(TagNames.MAPPING_FUNCTIONALITY)
    @DisplayName("Test mapping functionality")
    @TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
    inner class MappingFunctionality(val entityManager: TestEntityManager) {

        @Test
        fun `persist and find`() {
            val (user) = SyntheticEntities()

            entityManager.persistFlushFind(user) shouldBe user
        }

        @Test
        fun `persist communities on cascade`() {
            val (user, community) = SyntheticEntities()

            user.add(community)

            entityManager.persistAndFlush(user)

            entityManager.find(community.javaClass, user.communities.first().id) shouldBe community
        }

        @Test
        fun `do not delete communities on cascade`() {
            val (user, community) = SyntheticEntities()

            user.add(community)

            entityManager.persistAndFlush(user)

            // check that community got persisted
            entityManager.find(community.javaClass, community.id).shouldNotBeNull()

            entityManager.remove(user)

            // check that community wasn't removed
            entityManager.find(community.javaClass, community.id).shouldNotBeNull()
        }

        @Test
        fun `persist wish on cascade`() {
            val (user, _, wish) = SyntheticEntities()

            user.add(wish)

            entityManager.persistAndFlush(user)

            entityManager.find(wish.javaClass, user.wishes.first().id) shouldBe wish
        }

        @Test
        fun `do not delete wish on cascade`() {
            val (user, _, wish) = SyntheticEntities()

            user.add(wish)

            entityManager.persistAndFlush(user)

            // check that community got persisted
            entityManager.find(wish.javaClass, wish.id).shouldNotBeNull()

            entityManager.remove(user)

            // check that community wasn't removed
            entityManager.find(wish.javaClass, wish.id).shouldNotBeNull()
        }
    }
}
