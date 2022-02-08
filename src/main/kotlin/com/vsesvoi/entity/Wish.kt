package com.vsesvoi.entity

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
@Table(name = "wish")
class Wish(
    @Column(name = "title", nullable = false, length = 50)
    var title: String,

    @ManyToOne(optional = false, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumn(name = "community_id", nullable = false)
    var community: Community,

    @Column(name = "description")
    var description: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null

    @ManyToMany(mappedBy = "wishes", cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var users: MutableSet<User> = mutableSetOf()

    init {
        community.wishes.add(this)
    }

    fun add(user: User) {
        users.add(user)
        user.wishes.add(this)
    }

    fun remove(user: User) {
        users.remove(user)
        user.wishes.remove(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Wish

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

}