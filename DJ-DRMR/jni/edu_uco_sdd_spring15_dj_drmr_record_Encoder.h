/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class edu_uco_sdd_spring15_dj_drmr_record_Encoder */

#ifndef _Included_edu_uco_sdd_spring15_dj_drmr_record_Encoder
#define _Included_edu_uco_sdd_spring15_dj_drmr_record_Encoder
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     edu_uco_sdd_spring15_dj_drmr_record_Encoder
 * Method:    init
 * Signature: (IIIIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Encoder_init
  (JNIEnv *, jclass, jint, jint, jint, jint, jint, jstring, jstring, jstring, jstring, jstring);

/*
 * Class:     edu_uco_sdd_spring15_dj_drmr_record_Encoder
 * Method:    encode
 * Signature: (I[S[SI[B)I
 */
JNIEXPORT jint JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Encoder_encode
  (JNIEnv *, jclass, jint, jshortArray, jshortArray, jint, jbyteArray);

/*
 * Class:     edu_uco_sdd_spring15_dj_drmr_record_Encoder
 * Method:    encodeBufferInterleaved
 * Signature: (I[SI[B)I
 */
JNIEXPORT jint JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Encoder_encodeBufferInterleaved
  (JNIEnv *, jclass, jint, jshortArray, jint, jbyteArray);

/*
 * Class:     edu_uco_sdd_spring15_dj_drmr_record_Encoder
 * Method:    flush
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Encoder_flush
  (JNIEnv *, jclass, jint, jbyteArray);

/*
 * Class:     edu_uco_sdd_spring15_dj_drmr_record_Encoder
 * Method:    close
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_edu_uco_sdd_spring15_dj_1drmr_record_Encoder_close
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif