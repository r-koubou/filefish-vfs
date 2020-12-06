#include <windows.h>
#include <malloc.h>
#include <tchar.h>
#include "com_nullfish_lib_vfs_impl_local_native_lib_NativeFileUtil.h"

/*
 * Class:     com_nullfish_lib_vfs_impl_local_native_lib_NativeFileUtil
 * Method:    listFile
 * Signature: (Ljava/lang/String;)Lcom/nullfish/lib/vfs/impl/local/native_lib/LocalFileInfo;
 */
jobject JNICALL Java_com_nullfish_lib_vfs_impl_local_native_1lib_NativeFileUtil_listFile
  (JNIEnv *env, jclass clazz, jstring jstring_path)
{
	jobject wfd2fileInfo(JNIEnv *, WIN32_FIND_DATA);
	char *path;
	jclass fileInfoClass, permissionClass, attributeClass, stringClass;
	_TCHAR fileName[_MAX_PATH];
	_TCHAR pathName[_MAX_PATH];
	WIN32_FIND_DATA findData;
	HANDLE hFindFile, hHeap;
	
	fileInfoClass = env->FindClass("com/nullfish/lib/vfs/impl/local/native/LocalFileInfo");
	stringClass = env->FindClass("java/lang/String");

	path = env->GetStringUTFChars(jstring_path, NULL);
	_tcscpy(fileName, path);
	_tcscpy(pathName, path);
	env->ReleaseStringUTFChars(jstring_path, &path);

	hFindFile = FindFirstFile(fileName, findData);
	if (hFindFile == INVALID_HANDLE_VALUE) {
		if (GetLastError() == ERROR_FILE_NOT_FOUND) {
			// new FastFile[0]
			return env->NewObjectArray(0, fileInfoClass, NULL);
		} else {
			return NULL;		// I/O error
		}
	}

	
}
