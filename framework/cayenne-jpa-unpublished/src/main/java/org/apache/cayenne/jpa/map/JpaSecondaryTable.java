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
operator|.
name|map
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
name|javax
operator|.
name|persistence
operator|.
name|SecondaryTable
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
name|TreeNodeChild
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
name|XMLEncoder
import|;
end_import

begin_class
specifier|public
class|class
name|JpaSecondaryTable
extends|extends
name|JpaTable
block|{
specifier|protected
name|Collection
argument_list|<
name|JpaPrimaryKeyJoinColumn
argument_list|>
name|primaryKeyJoinColumns
decl_stmt|;
specifier|public
name|JpaSecondaryTable
parameter_list|()
block|{
block|}
specifier|public
name|JpaSecondaryTable
parameter_list|(
name|SecondaryTable
name|annotation
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|name
operator|=
name|annotation
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|catalog
argument_list|()
argument_list|)
condition|)
block|{
name|catalog
operator|=
name|annotation
operator|.
name|catalog
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|annotation
operator|.
name|schema
argument_list|()
argument_list|)
condition|)
block|{
name|schema
operator|=
name|annotation
operator|.
name|schema
argument_list|()
expr_stmt|;
block|}
name|getUniqueConstraints
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|annotation
operator|.
name|uniqueConstraints
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|uniqueConstraints
operator|.
name|add
argument_list|(
operator|new
name|JpaUniqueConstraint
argument_list|(
name|annotation
operator|.
name|uniqueConstraints
argument_list|()
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getPrimaryKeyJoinColumns
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|annotation
operator|.
name|pkJoinColumns
argument_list|()
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|primaryKeyJoinColumns
operator|.
name|add
argument_list|(
operator|new
name|JpaPrimaryKeyJoinColumn
argument_list|(
name|annotation
operator|.
name|pkJoinColumns
argument_list|()
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|TreeNodeChild
argument_list|(
name|type
operator|=
name|JpaPrimaryKeyJoinColumn
operator|.
name|class
argument_list|)
specifier|public
name|Collection
argument_list|<
name|JpaPrimaryKeyJoinColumn
argument_list|>
name|getPrimaryKeyJoinColumns
parameter_list|()
block|{
if|if
condition|(
name|primaryKeyJoinColumns
operator|==
literal|null
condition|)
block|{
name|primaryKeyJoinColumns
operator|=
operator|new
name|ArrayList
argument_list|<
name|JpaPrimaryKeyJoinColumn
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|primaryKeyJoinColumns
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|encodeAsXML
parameter_list|(
name|XMLEncoder
name|encoder
parameter_list|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|"<secondary-table"
argument_list|)
expr_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" name=\""
operator|+
name|name
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|catalog
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" catalog=\""
operator|+
name|catalog
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
literal|" schema=\""
operator|+
name|schema
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|println
argument_list|(
literal|'>'
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|indent
argument_list|(
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|primaryKeyJoinColumns
operator|!=
literal|null
condition|)
block|{
name|encoder
operator|.
name|print
argument_list|(
name|primaryKeyJoinColumns
argument_list|)
expr_stmt|;
block|}
name|encoder
operator|.
name|indent
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
name|encoder
operator|.
name|println
argument_list|(
literal|"</secondary-table>"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

