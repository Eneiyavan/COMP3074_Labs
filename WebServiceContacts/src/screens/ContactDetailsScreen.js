import React, { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  Alert,
  Linking,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from 'react-native';

import DetailRow from '../components/DetailRow';
import { getUser } from '../api/usersApi';

export default function ContactDetailsScreen({ route }) {
  const { userId } = route.params;

  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    loadUser();
  }, [userId]);

  async function loadUser() {
    try {
      setLoading(true);
      setError(null);
      const data = await getUser(userId);
      setUser(data);
    } catch (err) {
      setError(err.message || 'Unable to load contact.');
    } finally {
      setLoading(false);
    }
  }

  async function dialPhoneNumber(phoneNumber) {
    // API phones look like "1-770-736-8031 x56442": drop the extension and symbols
    const clean = phoneNumber.split('x')[0].replace(/[^\d+]/g, '');
    try {
      await Linking.openURL(`tel:${clean}`);
    } catch (err) {
      Alert.alert('Error', 'The dialer could not be opened.');
    }
  }

  if (loading) {
    return (
      <View style={styles.center}>
        <ActivityIndicator size="large" />
        <Text style={styles.message}>Loading contact...</Text>
      </View>
    );
  }

  if (error) {
    return (
      <View style={styles.center}>
        <Text style={styles.error}>Error: {error}</Text>
      </View>
    );
  }

  if (!user) {
    return null;
  }

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>{user.name}</Text>

      <DetailRow label="ID" value={user.id.toString()} />
      <DetailRow label="Username" value={user.username} />
      <DetailRow label="Email" value={user.email} />

      <Text style={styles.label}>Phone</Text>
      <Pressable onPress={() => dialPhoneNumber(user.phone)}>
        <Text style={styles.link}>{user.phone}</Text>
      </Pressable>

      <DetailRow label="Website" value={user.website} />

      <Text style={styles.section}>Address</Text>
      <DetailRow label="Street" value={user.address.street} />
      <DetailRow label="Suite" value={user.address.suite} />
      <DetailRow label="City" value={user.address.city} />
      <DetailRow label="Zip code" value={user.address.zipcode} />
      <DetailRow label="Latitude" value={user.address.geo.lat} />
      <DetailRow label="Longitude" value={user.address.geo.lng} />

      <Text style={styles.section}>Company</Text>
      <DetailRow label="Name" value={user.company.name} />
      <DetailRow label="Catch phrase" value={user.company.catchPhrase} />
      <DetailRow label="Business" value={user.company.bs} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 20, paddingBottom: 40 },
  center: { flex: 1, alignItems: 'center', justifyContent: 'center', padding: 24 },
  title: { fontSize: 26, fontWeight: '700', marginBottom: 18 },
  section: { fontSize: 20, fontWeight: '700', marginTop: 16, marginBottom: 10 },
  label: { fontSize: 13, fontWeight: '700' },
  link: {
    marginTop: 2,
    marginBottom: 10,
    fontSize: 16,
    color: '#0066cc',
    textDecorationLine: 'underline',
  },
  message: { marginTop: 12 },
  error: { color: '#b00020', textAlign: 'center' },
});
