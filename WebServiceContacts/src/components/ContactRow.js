import React from 'react';
import { Pressable, StyleSheet, Text } from 'react-native';

export default function ContactRow({ user, onPress }) {
  return (
    <Pressable
      onPress={onPress}
      style={({ pressed }) => [styles.row, pressed && styles.pressed]}
    >
      <Text style={styles.name}>{user.name}</Text>
      <Text style={styles.email}>{user.email}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  row: {
    paddingHorizontal: 18,
    paddingVertical: 14,
    borderBottomWidth: 1,
    borderBottomColor: '#dddddd',
  },
  pressed: { opacity: 0.55 },
  name: { fontSize: 18, fontWeight: '600' },
  email: { marginTop: 4, fontSize: 14 },
});
