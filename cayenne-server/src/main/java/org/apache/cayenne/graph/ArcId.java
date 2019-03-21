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
name|graph
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|parser
operator|.
name|ASTDbPath
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
name|reflect
operator|.
name|ArcProperty
import|;
end_import

begin_comment
comment|/**  * Object that represents Arc identifier.  * Used in graph change operations.  *  * @since 4.2  */
end_comment

begin_class
specifier|public
class|class
name|ArcId
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|3712846298213425259L
decl_stmt|;
specifier|private
specifier|final
name|String
name|forwardArc
decl_stmt|;
specifier|private
specifier|final
name|String
name|reverseArc
decl_stmt|;
specifier|public
name|ArcId
parameter_list|(
name|ArcProperty
name|property
parameter_list|)
block|{
name|this
operator|.
name|forwardArc
operator|=
name|property
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|reverseArc
operator|=
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
operator|==
literal|null
condition|?
name|ASTDbPath
operator|.
name|DB_PREFIX
operator|+
name|property
operator|.
name|getComplimentaryReverseDbRelationshipPath
argument_list|()
else|:
name|property
operator|.
name|getComplimentaryReverseArc
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
specifier|public
name|ArcId
parameter_list|(
name|String
name|forwardArc
parameter_list|,
name|String
name|reverseArc
parameter_list|)
block|{
name|this
operator|.
name|forwardArc
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|forwardArc
argument_list|)
expr_stmt|;
name|this
operator|.
name|reverseArc
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|reverseArc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getForwardArc
parameter_list|()
block|{
return|return
name|forwardArc
return|;
block|}
specifier|public
name|String
name|getReverseArc
parameter_list|()
block|{
return|return
name|reverseArc
return|;
block|}
specifier|public
name|ArcId
name|getReverseId
parameter_list|()
block|{
return|return
operator|new
name|ArcId
argument_list|(
name|reverseArc
argument_list|,
name|forwardArc
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
return|return
literal|false
return|;
name|ArcId
name|arcId
init|=
operator|(
name|ArcId
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|forwardArc
operator|.
name|equals
argument_list|(
name|arcId
operator|.
name|forwardArc
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
name|reverseArc
operator|.
name|equals
argument_list|(
name|arcId
operator|.
name|reverseArc
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
name|forwardArc
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
name|reverseArc
operator|.
name|hashCode
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|forwardArc
return|;
block|}
block|}
end_class

end_unit

