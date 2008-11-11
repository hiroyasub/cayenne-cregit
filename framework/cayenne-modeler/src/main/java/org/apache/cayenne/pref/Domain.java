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
name|pref
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Iterator
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
name|DataObjectUtils
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|DbAttribute
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
name|DbEntity
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
name|NamedQuery
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
comment|/**  * Preferences "domain" is logical area for preferences storage. Domains are organized in  * a tree hierarchy allowing cascading preferences lookup.  *   */
end_comment

begin_class
specifier|public
class|class
name|Domain
extends|extends
name|_Domain
block|{
comment|/**      * Renames this domain. If there is a sibling domain with same name, such sibling is      * renamed using generated unique name. This operation essentially substitutes one      * domain subtree with another.      */
specifier|public
name|void
name|rename
parameter_list|(
name|String
name|newName
parameter_list|)
block|{
if|if
condition|(
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getName
argument_list|()
argument_list|,
name|newName
argument_list|)
condition|)
block|{
return|return;
block|}
name|Domain
name|parent
init|=
name|getParentDomain
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
return|return;
block|}
name|Domain
name|other
init|=
name|parent
operator|.
name|getSubdomain
argument_list|(
name|newName
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|other
operator|!=
literal|null
operator|&&
name|other
operator|!=
name|this
condition|)
block|{
name|String
name|otherName
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|parent
operator|.
name|getSubdomain
argument_list|(
name|newName
operator|+
name|i
argument_list|,
literal|false
argument_list|)
operator|==
literal|null
condition|)
block|{
name|otherName
operator|=
name|newName
operator|+
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|otherName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|PreferenceException
argument_list|(
literal|"Can't rename an existing domain '"
operator|+
name|newName
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|other
operator|.
name|setName
argument_list|(
name|otherName
argument_list|)
expr_stmt|;
block|}
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a direct child of this domain that should handle preferences for all      * instances of a given Java class. Creates such subdomain if it doesn't exist.      */
specifier|public
name|Domain
name|getSubdomain
parameter_list|(
name|Class
name|javaClass
parameter_list|)
block|{
return|return
name|getSubdomain
argument_list|(
name|javaClass
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns named subdomain. Creates such subdomain if it doesn't exist. Named      * subdomains are useful when preferences have to be assigned based on a more      * fine-grained criteria than a Java class. E.g. a class Project can have preferences      * subdomain for each project location.      */
specifier|public
name|Domain
name|getSubdomain
parameter_list|(
name|String
name|subdomainName
parameter_list|)
block|{
return|return
name|getSubdomain
argument_list|(
name|subdomainName
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
name|Domain
name|getSubdomain
parameter_list|(
name|String
name|subdomainName
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|List
name|subdomains
init|=
name|getSubdomains
argument_list|()
decl_stmt|;
if|if
condition|(
name|subdomains
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|List
name|matching
init|=
name|ExpressionFactory
operator|.
name|matchExp
argument_list|(
name|Domain
operator|.
name|NAME_PROPERTY
argument_list|,
name|subdomainName
argument_list|)
operator|.
name|filterObjects
argument_list|(
name|subdomains
argument_list|)
decl_stmt|;
if|if
condition|(
name|matching
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
operator|(
name|Domain
operator|)
name|matching
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
if|if
condition|(
operator|!
name|create
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Domain
name|childSubdomain
init|=
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|Domain
operator|.
name|class
argument_list|)
decl_stmt|;
name|addToSubdomains
argument_list|(
name|childSubdomain
argument_list|)
expr_stmt|;
name|childSubdomain
operator|.
name|setName
argument_list|(
name|subdomainName
argument_list|)
expr_stmt|;
if|if
condition|(
name|getLevel
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null level, can't create child"
argument_list|)
throw|;
block|}
name|int
name|level
init|=
name|getLevel
argument_list|()
operator|.
name|intValue
argument_list|()
operator|+
literal|1
decl_stmt|;
name|childSubdomain
operator|.
name|setLevel
argument_list|(
operator|new
name|Integer
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
return|return
name|childSubdomain
return|;
block|}
comment|/**      * Returns all generic preferences for the domain.      */
specifier|public
name|List
name|getDetails
parameter_list|()
block|{
name|Collection
name|domainPrefs
init|=
name|getPreferences
argument_list|()
decl_stmt|;
if|if
condition|(
name|domainPrefs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// return mutable list
return|return
operator|new
name|ArrayList
argument_list|(
literal|1
argument_list|)
return|;
block|}
name|List
name|details
init|=
operator|new
name|ArrayList
argument_list|(
name|domainPrefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|domainPrefs
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DomainPreference
name|preference
init|=
operator|(
name|DomainPreference
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|details
operator|.
name|add
argument_list|(
name|preference
operator|.
name|getPreference
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|details
return|;
block|}
comment|/**      * Returns a preference object used to read/write properties.      */
specifier|public
name|PreferenceDetail
name|getDetail
parameter_list|(
name|String
name|key
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
return|return
name|getDetail
argument_list|(
name|key
argument_list|,
literal|null
argument_list|,
name|create
argument_list|)
return|;
block|}
comment|/**      * Returns all stored PreferenceDetails for a given class in this Domain.      */
specifier|public
name|Collection
name|getDetails
parameter_list|(
name|Class
name|javaClass
parameter_list|)
block|{
comment|// extract preference ids, and then fetch matching prefrences...
name|Collection
name|preferences
init|=
name|getPreferences
argument_list|()
decl_stmt|;
if|if
condition|(
name|preferences
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
name|Collection
name|ids
init|=
operator|new
name|ArrayList
argument_list|(
name|preferences
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|preferences
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|DomainPreference
name|pref
init|=
operator|(
name|DomainPreference
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ids
operator|.
name|add
argument_list|(
name|DataObjectUtils
operator|.
name|pkForObject
argument_list|(
name|pref
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ObjectContext
name|context
init|=
name|getObjectContext
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|context
operator|.
name|getEntityResolver
argument_list|()
operator|.
name|lookupDbEntity
argument_list|(
name|javaClass
argument_list|)
decl_stmt|;
name|DbAttribute
name|pk
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|Expression
name|qualifier
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"db:"
operator|+
name|pk
operator|.
name|getName
argument_list|()
operator|+
literal|" in $ids"
argument_list|)
decl_stmt|;
name|Map
name|params
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"ids"
argument_list|,
name|ids
argument_list|)
decl_stmt|;
name|SelectQuery
name|query
init|=
operator|new
name|SelectQuery
argument_list|(
name|javaClass
argument_list|,
name|qualifier
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|performQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**      * Returns preference details keyed using their master key.      */
specifier|public
name|Map
name|getDetailsMap
parameter_list|(
name|Class
name|javaClass
parameter_list|)
block|{
name|Collection
name|details
init|=
name|getDetails
argument_list|(
name|javaClass
argument_list|)
decl_stmt|;
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|details
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|map
return|;
block|}
name|Iterator
name|it
init|=
name|details
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|PreferenceDetail
name|detail
init|=
operator|(
name|PreferenceDetail
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|detail
operator|.
name|getKey
argument_list|()
argument_list|,
name|detail
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
comment|/**      * Locates a PreferenceDetail in a current Domain for a given key and Java class. If      * no such preference found, and "create" is true, a new preference is created. If an      * existing preference class does not match supplied class, PreferenceException is      * thrown.      */
specifier|public
name|PreferenceDetail
name|getDetail
parameter_list|(
name|String
name|key
parameter_list|,
name|Class
name|javaClass
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
name|DomainPreference
name|preferenceLink
init|=
name|getDomainPreference
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|preferenceLink
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|create
condition|)
block|{
return|return
literal|null
return|;
block|}
name|preferenceLink
operator|=
operator|(
name|DomainPreference
operator|)
name|getObjectContext
argument_list|()
operator|.
name|newObject
argument_list|(
name|DomainPreference
operator|.
name|class
argument_list|)
expr_stmt|;
name|preferenceLink
operator|.
name|setDomain
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|preferenceLink
operator|.
name|setKey
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|getObjectContext
argument_list|()
operator|.
name|commitChanges
argument_list|()
expr_stmt|;
block|}
return|return
operator|(
name|javaClass
operator|==
literal|null
operator|)
condition|?
name|preferenceLink
operator|.
name|getPreference
argument_list|()
else|:
name|preferenceLink
operator|.
name|getPreference
argument_list|(
name|javaClass
argument_list|,
name|create
argument_list|)
return|;
block|}
comment|/**      * Looks up a preference for key in the domain hierarchy.      */
name|DomainPreference
name|getDomainPreference
parameter_list|(
name|String
name|key
parameter_list|)
block|{
comment|// query sorts preferences by subdomain level, so the first object is the lowest
comment|// one
name|Map
name|params
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"domain"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|List
name|results
init|=
name|getObjectContext
argument_list|()
operator|.
name|performQuery
argument_list|(
operator|new
name|NamedQuery
argument_list|(
literal|"DomainPreferenceForKey"
argument_list|,
name|params
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|(
name|results
operator|.
name|size
argument_list|()
operator|<
literal|1
operator|)
condition|?
literal|null
else|:
operator|(
name|DomainPreference
operator|)
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
end_class

end_unit

