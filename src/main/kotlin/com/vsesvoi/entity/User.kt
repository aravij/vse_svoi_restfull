package com.vsesvoi.entity

import org.hibernate.Hibernate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Column(name = "name", nullable = false)
    var name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "user_communities",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "communities_id")]
    )
    var communities: MutableSet<Community> = mutableSetOf()

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinTable(
        name = "users_wish",
        joinColumns = [JoinColumn(name = "users_id")],
        inverseJoinColumns = [JoinColumn(name = "wish_id")]
    )
    var wishes: MutableSet<Wish> = mutableSetOf()

    fun add(community: Community) {
        communities.add(community)
        community.users.add(this)
    }

    fun remove(community: Community) {
        communities.remove(community)
        community.users.remove(this)
    }

    fun add(wish: Wish) {
        wishes.add(wish)
        wish.users.add(this)
    }

    fun remove(wish: Wish) {
        wishes.remove(wish)
        wish.users.remove(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
