begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|di
operator|.
name|DIRuntimeException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * The implementation here is basically an adjacency list, but a {@link Map} is  * used to map each vertex to its list of adjacent vertices.  *  * @param<V>  *            A type of a vertex.  * @since 4.0  */
end_comment

begin_class
class|class
name|DIGraph
parameter_list|<
name|V
parameter_list|>
block|{
comment|/** 	 * {@link LinkedHashMap} is used for supporting insertion order. 	 */
specifier|private
name|Map
argument_list|<
name|V
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|neighbors
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|DIGraph
parameter_list|()
block|{
block|}
comment|/** 	 * Add a vertex to the graph. Nothing happens if vertex is already in graph. 	 */
specifier|public
name|void
name|add
parameter_list|(
name|V
name|vertex
parameter_list|)
block|{
if|if
condition|(
name|neighbors
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
condition|)
block|{
return|return;
block|}
name|neighbors
operator|.
name|put
argument_list|(
name|vertex
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|V
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add vertexes to the graph.      */
specifier|public
name|void
name|addAll
parameter_list|(
name|Collection
argument_list|<
name|V
argument_list|>
name|vertexes
parameter_list|)
block|{
for|for
control|(
name|V
name|vertex
range|:
name|vertexes
control|)
block|{
name|this
operator|.
name|add
argument_list|(
name|vertex
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Add an edge to the graph; if either vertex does not exist, it's added. 	 * This implementation allows the creation of multi-edges and self-loops. 	 */
specifier|public
name|void
name|add
parameter_list|(
name|V
name|from
parameter_list|,
name|V
name|to
parameter_list|)
block|{
name|this
operator|.
name|add
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|this
operator|.
name|add
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|neighbors
operator|.
name|get
argument_list|(
name|from
argument_list|)
operator|.
name|add
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * True iff graph contains vertex. 	 */
specifier|public
name|boolean
name|contains
parameter_list|(
name|V
name|vertex
parameter_list|)
block|{
return|return
name|neighbors
operator|.
name|containsKey
argument_list|(
name|vertex
argument_list|)
return|;
block|}
comment|/** 	 * Remove an edge from the graph. Nothing happens if no such edge. 	 * 	 * @throws IllegalArgumentException 	 *             if either vertex doesn't exist. 	 */
specifier|public
name|void
name|remove
parameter_list|(
name|V
name|from
parameter_list|,
name|V
name|to
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|this
operator|.
name|contains
argument_list|(
name|from
argument_list|)
operator|&&
name|this
operator|.
name|contains
argument_list|(
name|to
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Nonexistent vertex"
argument_list|)
throw|;
block|}
name|neighbors
operator|.
name|get
argument_list|(
name|from
argument_list|)
operator|.
name|remove
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Return (as a Map) the out-degree of each vertex. 	 */
specifier|public
name|Map
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|outDegree
parameter_list|()
block|{
name|Map
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|result
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|V
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|neighbors
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/** 	 * Return (as a Map) the in-degree of each vertex. 	 */
specifier|public
name|Map
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|inDegree
parameter_list|()
block|{
name|Map
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|result
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|V
name|v
range|:
name|neighbors
operator|.
name|keySet
argument_list|()
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|v
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|V
name|from
range|:
name|neighbors
operator|.
name|keySet
argument_list|()
control|)
block|{
for|for
control|(
name|V
name|to
range|:
name|neighbors
operator|.
name|get
argument_list|(
name|from
argument_list|)
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|to
argument_list|,
name|result
operator|.
name|get
argument_list|(
name|to
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/** 	 * Return (as a List) the topological sort of the vertices. Throws an exception if cycles are detected. 	 */
specifier|public
name|List
argument_list|<
name|V
argument_list|>
name|topSort
parameter_list|()
block|{
name|Map
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|degree
init|=
name|inDegree
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|V
argument_list|>
name|zeroDegree
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|LinkedList
argument_list|<
name|V
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|V
argument_list|,
name|Integer
argument_list|>
name|entry
range|:
name|degree
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|0
condition|)
block|{
name|zeroDegree
operator|.
name|push
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
while|while
condition|(
operator|!
name|zeroDegree
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|V
name|v
init|=
name|zeroDegree
operator|.
name|pop
argument_list|()
decl_stmt|;
name|result
operator|.
name|push
argument_list|(
name|v
argument_list|)
expr_stmt|;
for|for
control|(
name|V
name|neighbor
range|:
name|neighbors
operator|.
name|get
argument_list|(
name|v
argument_list|)
control|)
block|{
name|degree
operator|.
name|put
argument_list|(
name|neighbor
argument_list|,
name|degree
operator|.
name|get
argument_list|(
name|neighbor
argument_list|)
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|degree
operator|.
name|get
argument_list|(
name|neighbor
argument_list|)
operator|==
literal|0
condition|)
block|{
name|zeroDegree
operator|.
name|push
argument_list|(
name|neighbor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Check that we have used the entire graph (if not, there was a cycle)
if|if
condition|(
name|result
operator|.
name|size
argument_list|()
operator|!=
name|neighbors
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|DIRuntimeException
argument_list|(
literal|"Dependency cycle detected in DI container"
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
comment|/** 	 * String representation of graph. 	 */
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|s
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|V
argument_list|,
name|List
argument_list|<
name|V
argument_list|>
argument_list|>
name|entry
range|:
name|neighbors
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|s
operator|.
name|append
argument_list|(
literal|"\n    "
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|" -> "
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|neighbors
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

