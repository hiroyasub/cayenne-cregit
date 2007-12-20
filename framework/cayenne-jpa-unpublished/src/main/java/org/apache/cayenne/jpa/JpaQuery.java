begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|jpa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|FlushModeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NoResultException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|NonUniqueResultException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TemporalType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|TransactionRequiredException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|ObjectContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|QueryResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|EJBQLQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|ParameterizedQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|ProcedureQuery
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SQLTemplate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|SelectQuery
import|;
end_import

begin_comment
comment|/**  * A JPA Query that wraps a Cayenne Query.  */
end_comment

begin_class
specifier|public
class|class
name|JpaQuery
implements|implements
name|Query
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|protected
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
name|cayenneQuery
decl_stmt|;
specifier|protected
name|ObjectContext
name|context
decl_stmt|;
specifier|public
name|JpaQuery
parameter_list|(
name|ObjectContext
name|ctxt
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|ctxt
expr_stmt|;
block|}
comment|/**      * Construct a named query.      */
specifier|public
name|JpaQuery
parameter_list|(
name|ObjectContext
name|context
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
name|q
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupQuery
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|q
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Non-existing query: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|setQuery
argument_list|(
name|q
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setQuery
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
name|q
parameter_list|)
block|{
name|this
operator|.
name|cayenneQuery
operator|=
name|q
expr_stmt|;
block|}
specifier|protected
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
name|getQuery
parameter_list|()
block|{
return|return
name|cayenneQuery
return|;
block|}
comment|/**      * Return the same query with parameters set.      */
specifier|private
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|query
operator|.
name|Query
name|queryWithParameters
parameter_list|()
block|{
if|if
condition|(
name|parameters
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|cayenneQuery
return|;
block|}
return|return
operator|(
operator|(
name|ParameterizedQuery
operator|)
name|cayenneQuery
operator|)
operator|.
name|createQuery
argument_list|(
name|parameters
argument_list|)
return|;
block|}
comment|/**      * Execute a SELECT query and return the query results as a List.      *       * @return a list of the results      * @throws IllegalStateException if called for an EJB QL UPDATE or DELETE statement      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
name|getResultList
parameter_list|()
block|{
return|return
name|context
operator|.
name|performQuery
argument_list|(
name|queryWithParameters
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Execute an update or delete statement.      *       * @return the number of entities updated or deleted      * @throws IllegalStateException if called for an EJB QL SELECT statement      * @throws TransactionRequiredException if there is no transaction      */
specifier|public
name|int
name|executeUpdate
parameter_list|()
block|{
comment|// TODO: check transaction
name|QueryResponse
name|response
init|=
name|context
operator|.
name|performGenericQuery
argument_list|(
name|queryWithParameters
argument_list|()
argument_list|)
decl_stmt|;
name|int
index|[]
name|res
init|=
name|response
operator|.
name|firstUpdateCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|res
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|int
name|num
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|res
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|num
operator|=
name|num
operator|+
name|res
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|num
return|;
block|}
comment|/**      * Execute a SELECT query that returns a single result.      *       * @return the result      * @throws NoResultException if there is no result      * @throws NonUniqueResultException if more than one result      * @throws IllegalStateException if called for an EJB QL UPDATE or DELETE statement      */
specifier|public
name|Object
name|getSingleResult
parameter_list|()
block|{
name|List
argument_list|<
name|?
argument_list|>
name|rows
init|=
name|getResultList
argument_list|()
decl_stmt|;
if|if
condition|(
name|rows
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|NoResultException
argument_list|()
throw|;
block|}
if|if
condition|(
name|rows
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|NonUniqueResultException
argument_list|()
throw|;
block|}
return|return
name|rows
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Set the maximum number of results to retrieve.      *       * @param maxResult      * @return the same query instance      * @throws IllegalArgumentException if argument is negative      */
specifier|public
name|Query
name|setMaxResults
parameter_list|(
name|int
name|maxResult
parameter_list|)
block|{
if|if
condition|(
name|maxResult
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid max results value: "
operator|+
name|maxResult
argument_list|)
throw|;
block|}
name|Object
name|query
init|=
name|getQuery
argument_list|()
decl_stmt|;
comment|// the first two types are probably the only queries anyone would run via JPA
if|if
condition|(
name|query
operator|instanceof
name|EJBQLQuery
condition|)
block|{
operator|(
operator|(
name|EJBQLQuery
operator|)
name|query
operator|)
operator|.
name|setFetchLimit
argument_list|(
name|maxResult
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|SQLTemplate
condition|)
block|{
operator|(
operator|(
name|SQLTemplate
operator|)
name|query
operator|)
operator|.
name|setFetchLimit
argument_list|(
name|maxResult
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|SelectQuery
condition|)
block|{
operator|(
operator|(
name|SelectQuery
operator|)
name|query
operator|)
operator|.
name|setFetchLimit
argument_list|(
name|maxResult
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|query
operator|instanceof
name|ProcedureQuery
condition|)
block|{
operator|(
operator|(
name|ProcedureQuery
operator|)
name|query
operator|)
operator|.
name|setFetchLimit
argument_list|(
name|maxResult
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"query does not support maxResult: "
operator|+
name|query
argument_list|)
throw|;
block|}
return|return
name|this
return|;
block|}
specifier|public
name|Query
name|setFlushMode
parameter_list|(
name|FlushModeType
name|flushModeType
parameter_list|)
block|{
return|return
name|this
return|;
block|}
comment|/**      * Set an implementation-specific hint. If the hint name is not recognized, it is      * silently ignored.      *       * @param hintName      * @param value      * @return the same query instance      * @throws IllegalArgumentException if the second argument is not valid for the      *             implementation      */
specifier|public
name|Query
name|setHint
parameter_list|(
name|String
name|hintName
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|this
return|;
block|}
comment|/**      * Set the position of the first result to retrieve.      *       * @param startPosition position of the first result, numbered from 0      * @return the same query instance      * @throws IllegalArgumentException if argument is negative      */
specifier|public
name|Query
name|setFirstResult
parameter_list|(
name|int
name|startPosition
parameter_list|)
block|{
if|if
condition|(
name|startPosition
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid first result value: "
operator|+
name|startPosition
argument_list|)
throw|;
block|}
comment|// TODO: support in core like fetchLimit?
comment|// TODO: hack a temp solution here based on sub-list?
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
comment|/**      * Bind an argument to a named parameter.      *       * @param name the parameter name      * @param value      * @return the same query instance      * @throws IllegalArgumentException if parameter name does not correspond to parameter      *             in query string or argument is of incorrect type      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|cayenneQuery
operator|instanceof
name|ParameterizedQuery
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"query does not accept parameters"
argument_list|)
throw|;
block|}
comment|// TODO: check for valid parameter. should probably be built in to
comment|// all ParameterizedQuerys
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Bind an instance of java.util.Date to a named parameter.      *       * @param name      * @param value      * @param temporalType      * @return the same query instance      * @throws IllegalArgumentException if parameter name does not correspond to parameter      *             in query string      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Date
name|value
parameter_list|,
name|TemporalType
name|temporalType
parameter_list|)
block|{
comment|// handled by cayenne.
return|return
name|setParameter
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Bind an instance of java.util.Calendar to a named parameter.      *       * @param name      * @param value      * @param temporalType      * @return the same query instance      * @throws IllegalArgumentException if parameter name does not correspond to parameter      *             in query string      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Calendar
name|value
parameter_list|,
name|TemporalType
name|temporalType
parameter_list|)
block|{
comment|// handled by cayenne.
return|return
name|setParameter
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Bind an argument to a positional parameter.      *       * @param position      * @param value      * @return the same query instance      * @throws IllegalArgumentException if position does not correspond to positional      *             parameter of query or argument is of incorrect type      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|int
name|position
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
comment|// TODO: implement
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
comment|/**      * Bind an instance of java.util.Date to a positional parameter.      *       * @param position      * @param value      * @param temporalType      * @return the same query instance      * @throws IllegalArgumentException if position does not correspond to positional      *             parameter of query      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|int
name|position
parameter_list|,
name|Date
name|value
parameter_list|,
name|TemporalType
name|temporalType
parameter_list|)
block|{
comment|// handled by cayenne.
return|return
name|setParameter
argument_list|(
name|position
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**      * Bind an instance of java.util.Calendar to a positional parameter.      *       * @param position      * @param value      * @param temporalType      * @return the same query instance      * @throws IllegalArgumentException if position does not correspond to positional      *             parameter of query      */
specifier|public
name|Query
name|setParameter
parameter_list|(
name|int
name|position
parameter_list|,
name|Calendar
name|value
parameter_list|,
name|TemporalType
name|temporalType
parameter_list|)
block|{
comment|// handled by cayenne.
return|return
name|setParameter
argument_list|(
name|position
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

