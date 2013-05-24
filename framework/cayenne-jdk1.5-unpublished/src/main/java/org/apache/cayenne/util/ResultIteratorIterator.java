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
name|Iterator
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
name|ResultIterator
import|;
end_import

begin_class
specifier|public
class|class
name|ResultIteratorIterator
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Iterator
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|ResultIterator
argument_list|<
name|T
argument_list|>
name|parent
decl_stmt|;
specifier|public
name|ResultIteratorIterator
parameter_list|(
name|ResultIterator
argument_list|<
name|T
argument_list|>
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
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|parent
operator|.
name|hasNextRow
argument_list|()
return|;
block|}
specifier|public
name|T
name|next
parameter_list|()
block|{
return|return
name|parent
operator|.
name|nextRow
argument_list|()
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// TODO: hmm... JDBC ResultSet does support in-place remove
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"'remove' is not supported"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

