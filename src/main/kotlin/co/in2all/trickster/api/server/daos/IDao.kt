package co.in2all.trickster.api.server.daos

interface IDao {
    fun <E> getAll(): List<E>
    fun <E, K> getEntityById(id: K): E
    fun <E> update(entity: E): Unit
}