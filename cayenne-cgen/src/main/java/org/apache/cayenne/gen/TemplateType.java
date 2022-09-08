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
name|gen
package|;
end_package

begin_comment
comment|/**  * Defines class generation template types.  *   * @since 3.0  */
end_comment

begin_enum
specifier|public
enum|enum
name|TemplateType
block|{
name|ENTITY_SINGLE_CLASS
argument_list|(
literal|false
argument_list|,
literal|"Single Entity Class"
argument_list|,
literal|"singleclass"
argument_list|)
block|,
name|ENTITY_SUPERCLASS
argument_list|(
literal|true
argument_list|,
literal|"Entity Superclass"
argument_list|,
literal|"superclass"
argument_list|)
block|,
name|ENTITY_SUBCLASS
argument_list|(
literal|false
argument_list|,
literal|"Entity Subclass"
argument_list|,
literal|"subclass"
argument_list|)
block|,
name|EMBEDDABLE_SINGLE_CLASS
argument_list|(
literal|false
argument_list|,
literal|"Single Embeddable Class"
argument_list|,
literal|"embeddable-singleclass"
argument_list|)
block|,
name|EMBEDDABLE_SUPERCLASS
argument_list|(
literal|true
argument_list|,
literal|"Embeddable Superclass"
argument_list|,
literal|"embeddable-superclass"
argument_list|)
block|,
name|EMBEDDABLE_SUBCLASS
argument_list|(
literal|false
argument_list|,
literal|"Embeddable Subclass"
argument_list|,
literal|"embeddable-subclass"
argument_list|)
block|,
name|DATAMAP_SINGLE_CLASS
argument_list|(
literal|false
argument_list|,
literal|"Single DataMap Class"
argument_list|,
literal|"datamap-singleclass"
argument_list|)
block|,
name|DATAMAP_SUPERCLASS
argument_list|(
literal|true
argument_list|,
literal|"DataMap Superclass"
argument_list|,
literal|"datamap-superclass"
argument_list|)
block|,
name|DATAMAP_SUBCLASS
argument_list|(
literal|false
argument_list|,
literal|"DataMap Subclass"
argument_list|,
literal|"datamap-subclass"
argument_list|)
block|;
specifier|private
specifier|final
name|boolean
name|superclass
decl_stmt|;
specifier|private
specifier|final
name|String
name|readableName
decl_stmt|;
specifier|private
specifier|final
name|String
name|fileName
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSION
init|=
literal|".vm"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEMPLATES_DIR
init|=
literal|"templates/v4_1/"
decl_stmt|;
name|TemplateType
parameter_list|(
name|boolean
name|superclass
parameter_list|,
name|String
name|readableName
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|superclass
operator|=
name|superclass
expr_stmt|;
name|this
operator|.
name|readableName
operator|=
name|readableName
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSuperclass
parameter_list|()
block|{
return|return
name|superclass
return|;
block|}
specifier|public
name|String
name|readableName
parameter_list|()
block|{
return|return
name|readableName
return|;
block|}
specifier|public
name|String
name|fileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
specifier|public
name|String
name|fullFileName
parameter_list|()
block|{
return|return
name|fileName
operator|+
name|EXTENSION
return|;
block|}
specifier|public
name|String
name|pathFromSourceRoot
parameter_list|()
block|{
return|return
name|TEMPLATES_DIR
operator|+
name|fileName
operator|+
name|EXTENSION
return|;
block|}
specifier|public
specifier|static
name|TemplateType
name|byName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|TemplateType
name|templateType
range|:
name|TemplateType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|templateType
operator|.
name|readableName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|templateType
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
specifier|static
name|TemplateType
name|byPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
for|for
control|(
name|TemplateType
name|templateType
range|:
name|TemplateType
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|templateType
operator|.
name|pathFromSourceRoot
argument_list|()
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|templateType
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit

