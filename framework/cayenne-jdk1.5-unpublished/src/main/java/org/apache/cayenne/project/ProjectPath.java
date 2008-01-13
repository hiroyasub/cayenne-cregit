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
name|project
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

begin_comment
comment|/**  * Immutable holder of a selection path within a Cayenne project. Mostly used  * by various tools (CayenneModeler comes to mind) to navigate Cayenne  * projects. Contains a number of convenience methods to access path elements.  *   *<p>  * For instance, given a path<code>Project -> DataMap -> ObjEntity -> ObjAttribute</code>,  *<code>getObject</code> will return ObjAttribute,<code>getObjectParent</code>-  * ObjEntity,<code>getRoot</code>- Project.  *</p>  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|ProjectPath
block|{
specifier|public
specifier|static
specifier|final
name|Object
index|[]
name|EMPTY_PATH
init|=
operator|new
name|Object
index|[
literal|0
index|]
decl_stmt|;
specifier|protected
name|Object
index|[]
name|path
decl_stmt|;
specifier|public
name|ProjectPath
parameter_list|()
block|{
name|path
operator|=
name|EMPTY_PATH
expr_stmt|;
block|}
comment|/**      * Constructor for ProjectPath.      */
specifier|public
name|ProjectPath
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|path
operator|=
operator|new
name|Object
index|[]
block|{
name|object
block|}
expr_stmt|;
block|}
comment|/**      * Constructor for ProjectPath.      */
specifier|public
name|ProjectPath
parameter_list|(
name|Object
index|[]
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
operator|(
name|path
operator|!=
literal|null
operator|)
condition|?
name|path
else|:
name|EMPTY_PATH
expr_stmt|;
block|}
specifier|public
name|Object
index|[]
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|length
operator|==
literal|0
return|;
block|}
comment|/**      * Scans path, looking for the first element that is an instanceof<code>aClass</code>.      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|firstInstanceOf
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|path
index|[
name|i
index|]
operator|!=
literal|null
operator|&&
name|aClass
operator|.
name|isAssignableFrom
argument_list|(
name|path
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|path
index|[
name|i
index|]
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns an instance of the path, expanding this one by appending an      * object at the end.      */
specifier|public
name|ProjectPath
name|appendToPath
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|!=
literal|null
condition|)
block|{
name|Object
index|[]
name|newPath
init|=
operator|new
name|Object
index|[
name|path
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|arraycopy
argument_list|(
name|path
argument_list|,
literal|0
argument_list|,
name|newPath
argument_list|,
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
name|newPath
index|[
name|path
operator|.
name|length
index|]
operator|=
name|object
expr_stmt|;
return|return
operator|new
name|ProjectPath
argument_list|(
name|newPath
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|this
return|;
block|}
block|}
comment|/**      *       * @since 1.1      */
specifier|public
name|ProjectPath
name|subpathWithSize
parameter_list|(
name|int
name|subpathSize
parameter_list|)
block|{
if|if
condition|(
name|subpathSize
operator|<=
literal|0
condition|)
block|{
return|return
operator|new
name|ProjectPath
argument_list|()
return|;
block|}
if|else if
condition|(
name|subpathSize
operator|==
name|path
operator|.
name|length
condition|)
block|{
return|return
name|this
return|;
block|}
if|if
condition|(
name|subpathSize
operator|>
name|path
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
literal|"Subpath is longer than this path "
operator|+
name|subpathSize
operator|+
literal|" components. Path size: "
operator|+
name|path
operator|.
name|length
argument_list|)
throw|;
block|}
name|Object
index|[]
name|newPath
init|=
operator|new
name|Object
index|[
name|subpathSize
index|]
decl_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|path
argument_list|,
literal|0
argument_list|,
name|newPath
argument_list|,
literal|0
argument_list|,
name|subpathSize
argument_list|)
expr_stmt|;
return|return
operator|new
name|ProjectPath
argument_list|(
name|newPath
argument_list|)
return|;
block|}
comment|/**      * Returns a subpath to the first occurance of an object.      *       * @since 1.1      */
specifier|public
name|ProjectPath
name|subpathOfObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|path
index|[
name|i
index|]
operator|==
name|object
condition|)
block|{
comment|// strip remaining objects
return|return
name|subpathWithSize
argument_list|(
name|i
operator|+
literal|1
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Returns the root or starting object of the path.      */
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
if|if
condition|(
name|path
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|path
index|[
literal|0
index|]
return|;
block|}
comment|/**      * Returns the last object in the path.      */
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
if|if
condition|(
name|path
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// return last object
return|return
name|path
index|[
name|path
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
comment|/**      * Returns an object corresponding to the parent node of the node      * represented by the path. This is the object next to last object in the      * path.      */
specifier|public
name|Object
name|getObjectParent
parameter_list|()
block|{
if|if
condition|(
name|path
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// return next to last object
return|return
operator|(
name|path
operator|.
name|length
operator|>
literal|1
operator|)
condition|?
name|path
index|[
name|path
operator|.
name|length
operator|-
literal|2
index|]
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"[ProjectPath: "
argument_list|)
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
name|path
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|i
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|String
name|token
init|=
operator|(
name|path
index|[
name|i
index|]
operator|!=
literal|null
operator|)
condition|?
name|path
index|[
name|i
index|]
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
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
name|object
operator|==
name|this
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
name|ProjectPath
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectPath
name|otherPath
init|=
operator|(
name|ProjectPath
operator|)
name|object
decl_stmt|;
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|getPath
argument_list|()
argument_list|,
name|otherPath
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

