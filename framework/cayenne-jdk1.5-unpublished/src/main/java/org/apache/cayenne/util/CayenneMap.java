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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|FastTreeMap
import|;
end_import

begin_comment
comment|/**  * A<code>CayenneMap</code> is a specialized double-linked sorted map class. Attempts  * to add objects using an already existing keys will result in IllegalArgumentExceptions.  * Any added entries that implement CayenneMapEntry interface will have their parent set  * to the parent of this map.  *<p>  * CayenneMap is not subclassed directly, but is rather used as an instance variable  * within another class. Enclosing instance would set itself as a parent of this map.  *</p>  *   * @author Andrus Adamchik  * @deprecated since 3.0 this map is not used by Cayenne internally.  */
end_comment

begin_comment
comment|// WARNING: CayenneMap is not serializable via Hessian serialization mechanism used by
end_comment

begin_comment
comment|// CayenneConnector implementation.
end_comment

begin_comment
comment|// TODO: deprecate this ugly map. it is only used in Configuration
end_comment

begin_class
specifier|public
class|class
name|CayenneMap
extends|extends
name|FastTreeMap
block|{
specifier|protected
name|Object
name|parent
decl_stmt|;
comment|/**      * Constructor for CayenneMap.      */
specifier|public
name|CayenneMap
parameter_list|(
name|Object
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|/**      * Constructor for CayenneMap.      *       * @param c      */
specifier|public
name|CayenneMap
parameter_list|(
name|Object
name|parent
parameter_list|,
name|Comparator
name|c
parameter_list|)
block|{
name|super
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
block|}
comment|/**      * Constructor for CayenneMap.      *       * @param m      */
specifier|public
name|CayenneMap
parameter_list|(
name|Object
name|parent
parameter_list|,
name|Map
name|m
parameter_list|)
block|{
comment|// !IMPORTANT - set parent before populating the map
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|putAll
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for CayenneMap.      *       * @param m      */
specifier|public
name|CayenneMap
parameter_list|(
name|Object
name|parent
parameter_list|,
name|SortedMap
name|m
parameter_list|)
block|{
comment|// !IMPORTANT - set parent before populating the map
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|putAll
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
comment|/**      * Maps specified key-value pair. If value is a CayenneMapEntry, sets its parent to      * this map.      *       * @see java.util.Map#put(Object, Object)      */
annotation|@
name|Override
specifier|public
name|Object
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|containsKey
argument_list|(
name|key
argument_list|)
operator|&&
name|get
argument_list|(
name|key
argument_list|)
operator|!=
name|value
condition|)
block|{
comment|// build descriptive failure message
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"Attempt to insert duplicate key. [key '"
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|message
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
if|if
condition|(
name|parent
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|", parent '"
argument_list|)
operator|.
name|append
argument_list|(
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|parent
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
name|message
operator|.
name|append
argument_list|(
literal|", child '"
argument_list|)
operator|.
name|append
argument_list|(
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|value
operator|)
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|message
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|CayenneMapEntry
condition|)
block|{
operator|(
operator|(
name|CayenneMapEntry
operator|)
name|value
operator|)
operator|.
name|setParent
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|/**      * @see java.util.Map#putAll(Map)      */
annotation|@
name|Override
specifier|public
name|void
name|putAll
parameter_list|(
name|Map
name|t
parameter_list|)
block|{
name|Iterator
name|it
init|=
name|t
operator|.
name|entrySet
argument_list|()
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
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the parent.      *       * @return Object      */
specifier|public
name|Object
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
specifier|public
name|void
name|setParent
parameter_list|(
name|Object
name|mapParent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|mapParent
expr_stmt|;
block|}
block|}
end_class

end_unit

