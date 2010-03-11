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
name|query
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Map
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
name|CayenneRuntimeException
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
name|Persistent
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
name|map
operator|.
name|EntityResolver
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
name|util
operator|.
name|EqualsBuilder
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
name|util
operator|.
name|HashCodeBuilder
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * A query that is a reference to a named parameterized query stored in the mapping. The  * actual query is resolved during execution.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|NamedQuery
extends|extends
name|IndirectQuery
block|{
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
specifier|protected
name|boolean
name|forceNoCache
decl_stmt|;
specifier|protected
name|BaseQueryMetadata
name|overrideMetadata
decl_stmt|;
comment|// metadata fields...
specifier|transient
name|int
name|hashCode
decl_stmt|;
comment|//to enable serialization
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|NamedQuery
parameter_list|()
block|{
block|}
specifier|public
name|NamedQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NamedQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
comment|// copy parameters map (among other things to make hessian serialization work).
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|parameters
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates NamedQuery with parameters passed as two matching arrays of keys and      * values.      */
specifier|public
name|NamedQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|String
index|[]
name|keys
parameter_list|,
name|Object
index|[]
name|values
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|Util
operator|.
name|toMap
argument_list|(
name|keys
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|QueryMetadata
name|base
init|=
name|overrideMetadata
operator|!=
literal|null
condition|?
name|overrideMetadata
else|:
name|super
operator|.
name|getMetaData
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|QueryMetadataWrapper
name|wrapper
init|=
operator|new
name|QueryMetadataWrapper
argument_list|(
name|base
argument_list|)
decl_stmt|;
comment|// override cache policy, forcing refresh if needed
if|if
condition|(
name|forceNoCache
condition|)
block|{
name|QueryCacheStrategy
name|strategy
init|=
name|base
operator|.
name|getCacheStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE
operator|==
name|strategy
condition|)
block|{
name|wrapper
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|,
name|QueryCacheStrategy
operator|.
name|LOCAL_CACHE_REFRESH
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE
operator|==
name|strategy
condition|)
block|{
name|wrapper
operator|.
name|override
argument_list|(
name|QueryMetadata
operator|.
name|CACHE_STRATEGY_PROPERTY
argument_list|,
name|QueryCacheStrategy
operator|.
name|SHARED_CACHE_REFRESH
argument_list|)
expr_stmt|;
block|}
block|}
comment|// override cache key to include parameters
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
operator|&&
name|replacementQuery
operator|instanceof
name|NamedQuery
operator|&&
name|base
operator|.
name|getCacheKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// TODO: andrus, 3/29/2006 this is taken from SelectQuery...probably need a
comment|// central place for converting parameters to a cache key
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|parameters
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|wrapper
operator|.
name|override
argument_list|(
name|QueryMetadataWrapper
operator|.
name|CACHE_KEY_PROPERTY
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|wrapper
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Query
name|createReplacementQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|Query
name|query
init|=
name|resolveQuery
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|ParameterizedQuery
condition|)
block|{
name|query
operator|=
operator|(
operator|(
name|ParameterizedQuery
operator|)
name|query
operator|)
operator|.
name|createQuery
argument_list|(
name|normalizedParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|query
return|;
block|}
comment|/**      * Returns a non-null parameter map, substituting all persistent objects in the      * initial map with ObjectIds. This is needed so that a query could work uniformly on      * the server and client sides.      */
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|normalizedParameters
parameter_list|()
block|{
if|if
condition|(
name|parameters
operator|==
literal|null
operator|||
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_MAP
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|substitutes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|Persistent
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|Persistent
operator|)
name|value
operator|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
name|substitutes
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|substitutes
return|;
block|}
comment|/**      * Returns a query for name, throwing an exception if such query is not mapped in the      * EntityResolver.      */
specifier|protected
name|Query
name|resolveQuery
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|Query
name|query
init|=
name|resolver
operator|.
name|lookupQuery
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't find named query for name '"
operator|+
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|query
operator|==
name|this
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Named query resolves to self: '"
operator|+
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|query
return|;
block|}
comment|/**      * Overrides toString() outputting a short string with query class and name.      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|className
init|=
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|Util
operator|.
name|stripPackageName
argument_list|(
name|className
argument_list|)
operator|+
literal|":"
operator|+
name|getName
argument_list|()
return|;
block|}
comment|/**      * Initializes metadata overrides. Needed to store the metadata for the remote query      * proxies that have no access to the actual query.      */
specifier|public
name|void
name|initMetadata
parameter_list|(
name|QueryMetadata
name|metadata
parameter_list|)
block|{
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|overrideMetadata
operator|=
operator|new
name|BaseQueryMetadata
argument_list|()
expr_stmt|;
name|this
operator|.
name|overrideMetadata
operator|.
name|copyFromInfo
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|overrideMetadata
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * An object is considered equal to this NamedQuery if it is a NamedQuery with the      * same queryName and same parameters.      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|object
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|object
operator|instanceof
name|NamedQuery
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|NamedQuery
name|query
init|=
operator|(
name|NamedQuery
operator|)
name|object
decl_stmt|;
if|if
condition|(
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|name
argument_list|,
name|query
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|query
operator|.
name|parameters
operator|==
literal|null
operator|&&
name|parameters
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|query
operator|.
name|parameters
operator|==
literal|null
operator|||
name|parameters
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|query
operator|.
name|parameters
operator|.
name|size
argument_list|()
operator|!=
name|parameters
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EqualsBuilder
name|builder
init|=
operator|new
name|EqualsBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|parameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|entryKey
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|entryValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|entryValue
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|query
operator|.
name|parameters
operator|.
name|get
argument_list|(
name|entryKey
argument_list|)
operator|!=
literal|null
operator|||
operator|!
name|query
operator|.
name|parameters
operator|.
name|containsKey
argument_list|(
name|entryKey
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// takes care of comparing primitive arrays, such as byte[]
name|builder
operator|.
name|append
argument_list|(
name|entryValue
argument_list|,
name|query
operator|.
name|parameters
operator|.
name|get
argument_list|(
name|entryKey
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|builder
operator|.
name|isEquals
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Implements a standard hashCode contract considering custom 'equals' implementation.      */
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|hashCode
operator|==
literal|0
condition|)
block|{
name|HashCodeBuilder
name|builder
init|=
operator|new
name|HashCodeBuilder
argument_list|(
literal|13
argument_list|,
literal|17
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|name
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|parameters
operator|!=
literal|null
condition|)
block|{
name|Object
index|[]
name|keys
init|=
name|parameters
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|keys
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|keys
control|)
block|{
comment|// HashCodeBuilder will take care of processing object if it
comment|// happens to be a primitive array such as byte[]
name|builder
operator|.
name|append
argument_list|(
name|key
argument_list|)
operator|.
name|append
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|hashCode
operator|=
name|builder
operator|.
name|toHashCode
argument_list|()
expr_stmt|;
assert|assert
name|hashCode
operator|!=
literal|0
operator|:
literal|"Generated zero hashCode"
assert|;
block|}
return|return
name|hashCode
return|;
block|}
specifier|public
name|boolean
name|isForceNoCache
parameter_list|()
block|{
return|return
name|forceNoCache
return|;
block|}
specifier|public
name|void
name|setForceNoCache
parameter_list|(
name|boolean
name|forcingNoCache
parameter_list|)
block|{
name|this
operator|.
name|forceNoCache
operator|=
name|forcingNoCache
expr_stmt|;
block|}
block|}
end_class

end_unit

