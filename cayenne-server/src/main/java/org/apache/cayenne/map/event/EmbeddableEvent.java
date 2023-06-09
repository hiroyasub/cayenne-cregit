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
name|map
operator|.
name|event
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
name|map
operator|.
name|Embeddable
import|;
end_import

begin_class
specifier|public
class|class
name|EmbeddableEvent
extends|extends
name|MapEvent
block|{
specifier|protected
name|Embeddable
name|embeddable
decl_stmt|;
specifier|public
name|EmbeddableEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|setEmbeddable
argument_list|(
name|embeddable
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddableEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|Embeddable
name|embeddable2
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|embeddable2
argument_list|)
expr_stmt|;
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EmbeddableEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|Embeddable
name|embeddable2
parameter_list|,
name|String
name|oldClassName
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|embeddable2
argument_list|)
expr_stmt|;
name|setOldName
argument_list|(
name|oldClassName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setEmbeddable
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|)
block|{
name|this
operator|.
name|embeddable
operator|=
name|embeddable
expr_stmt|;
block|}
specifier|public
name|Embeddable
name|getEmbeddable
parameter_list|()
block|{
return|return
name|embeddable
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
operator|(
name|embeddable
operator|!=
literal|null
operator|)
condition|?
name|embeddable
operator|.
name|getClassName
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

